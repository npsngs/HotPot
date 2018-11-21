package com.grumpycat.hotpot.core;

import android.support.annotation.NonNull;

import com.grumpycat.hotpot.handler.FileHandler;
import com.grumpycat.hotpot.intercepter.RequestLogger;
import com.grumpycat.hotpot.ui.Messager;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by cc.he on 2018/8/22
 */
public class WebServer implements Runnable{
    private ServerSocket serverSocket;
    private HttpService httpService;
    private HttpParams params;
    private int port = 8888;
    private volatile boolean isLoop;
    private ExecutorService executor;
    private Messager messager;

    public WebServer(Messager messager) {
        this.messager = messager;
        init();
    }

    public void start() throws IOException {
        // 创建服务器套接字
        serverSocket = new ServerSocket(port);
        // 设置端口重用
        serverSocket.setReuseAddress(true);
        isLoop = true;

        executor = Executors.newFixedThreadPool(4, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread t = new Thread(r);
                t.setName("Hotspot-Worker");
                return t;
            }
        });


        Thread t = new Thread(this);
        t.setName("Hotspot-WebServer");
        t.setDaemon(true);
        t.start();
    }

    public void close(){
        isLoop = false;
    }

    @Override
    public void run() {
        try {
            //* 循环接收各客户端 *//*
            while (isLoop && !Thread.interrupted()) {
                // 接收客户端套接字
                Socket socket = serverSocket.accept();
                // 绑定至服务器端HTTP连接
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                conn.bind(socket, params);
                // 派送至WorkerThread处理请求
                executor.execute(new Worker(httpService, conn));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void init(){

        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        // 增加HTTP协议拦截器
        httpproc.addInterceptor(new RequestLogger(messager));

        httpproc.addInterceptor(new ResponseDate());
        httpproc.addInterceptor(new ResponseServer());
        httpproc.addInterceptor(new ResponseContent());
        httpproc.addInterceptor(new ResponseConnControl());

        // 创建HTTP服务
        httpService = new HttpService(httpproc,
                new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());
        // 创建HTTP参数
        params = new BasicHttpParams();
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "WebServer/1.1");
        // 设置HTTP参数
        httpService.setParams(params);
        // 创建HTTP请求执行器注册表
        HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
        // 增加HTTP请求执行器
        reqistry.register(UrlPattern.FILE, new FileHandler());
        // 设置HTTP请求执行器
        httpService.setHandlerResolver(reqistry);
    }


}
