package com.grumpycat.hotpot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.grumpycat.hotpot.core.WebServer;
import com.grumpycat.hotpot.ui.Messager;
import com.grumpycat.hotpot.util.IpGetUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements Messager{
    private WebServer webServer;
    private TextView tv_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv);
        tv_msg = findViewById(R.id.msg);
        tv.setText(IpGetUtil.getIPAddress(this));

        webServer = new WebServer(this);
        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webServer.close();
    }

    @Override
    public void sendMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_msg.setText(msg);
            }
        });
    }
}
