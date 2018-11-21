package com.grumpycat.hotpot.handler;

import com.grumpycat.hotpot.util.HttpUtil;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Created by cc.he on 2018/8/22
 */
public abstract class BaseHandler implements HttpRequestHandler {
    @Override
    public final void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpUtil.parseParamters(request.getRequestLine().getUri());
        onHandle(request, response, context, params);
    }


    protected abstract void onHandle(HttpRequest request, HttpResponse response, HttpContext context, Map<String, String> params) throws HttpException, IOException ;

}
