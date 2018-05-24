package com.example.lulin.todolist.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lulin.todolist.Service.AlarmService;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage("package_name");
        if (i != null) {
            context.startService(new Intent(context, AlarmService.class));
        }
    }
}
