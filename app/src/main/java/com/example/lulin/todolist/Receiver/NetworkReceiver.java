package com.example.lulin.todolist.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import cn.bmob.v3.Bmob;

public class NetworkReceiver extends BroadcastReceiver {

    private static final String APP_ID = "1c54d5b204e98654778c77547afc7a66";

    @Override
    public void onReceive(Context context, Intent intent) {
        //**判断当前的网络连接状态是否可用*/
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if ( info != null && info.isAvailable()){
            //当前网络状态可用
            Bmob.initialize(context, APP_ID);
            Log.i("网络状态", "网络已连接");
        }else {
            //当前网络不可用
            Log.i("网络状态", "无网络连接");
        }
    }
}
