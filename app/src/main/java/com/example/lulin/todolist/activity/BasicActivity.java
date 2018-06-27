package com.example.lulin.todolist.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Receiver.NetworkReceiver;

import cn.bmob.v3.Bmob;

/**
 * Activity基类:实时获取网络状态
 */
public class BasicActivity extends AppCompatActivity {
    private boolean isRegistered = false;
    private NetworkReceiver networkReceiver;
    private static final String APP_ID = "1c54d5b204e98654778c77547afc7a66";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getApplication(), APP_ID);
        //注册网络状态监听广播
        networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        isRegistered = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        if (isRegistered) {
            unregisterReceiver(networkReceiver);
        }
    }
}
