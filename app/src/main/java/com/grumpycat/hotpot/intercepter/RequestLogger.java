package com.grumpycat.hotpot.intercepter;

import com.grumpycat.hotpot.ui.Messager;
import com.grumpycat.hotpot.util.TimeUtil;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by cc.he on 2018/9/28
 */
public class RequestLogger implements HttpRequestInterceptor {
    private Messager messager;
    public RequestLogger(Messager messager) {
        this.messager = messager;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        if (messager != null){
            String msg = String.format("[%s]Request:\n%s",
                    TimeUtil.getCurrentTime(),
                    request.getRequestLine());
            HeaderIterator iterator = request.headerIterator();
            while (iterator.hasNext()){
                msg += "\n";
                Header header = iterator.nextHeader();
                msg += header.getName()+":"+header.getValue();
            }
            messager.sendMsg(msg);
        }
    }
}
