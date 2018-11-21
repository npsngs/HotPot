package com.grumpycat.hotpot.core;

import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;


/**
 * Created by cc.he on 2018/8/22
 */
public class Worker implements Runnable{
    private HttpService service;
    private HttpServerConnection conn;

    public Worker(HttpService service, HttpServerConnection conn) {
        this.service = service;
        this.conn = conn;
    }

    @Override
    public void run() {
        HttpContext context = new BasicHttpContext();
        try {
            service.handleRequest(conn, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
