package com.grumpycat.hotpot.handler;

import android.util.Log;

import com.grumpycat.hotpot.util.TimeUtil;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;

/**
 * Created by cc.he on 2018/8/22
 */
public class FileHandler extends BaseHandler {
    private static int count = 0;

    @Override
    protected void onHandle(HttpRequest request, HttpResponse response, HttpContext context, Map<String, String> params) throws HttpException, IOException {
        Log.e("request", "url:"+request.getRequestLine());
        String uri = request.getRequestLine().getUri();
        response.setEntity(new StringEntity("Fuck"+uri+" count="+count+" Time:["+ TimeUtil.getCurrentTime()+"]"));
        response.setStatusCode(200);
        count++;
    }
}
