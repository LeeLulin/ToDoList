package com.example.lulin.todolist.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.activity.MainActivity;
import com.example.lulin.todolist.utils.Todos;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager manager;
    private static final int NOTIFICATION_ID_1 = 0x00113;
    private String title;
    private String dsc;
    private static final String TAG = "receiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        //此处接收闹钟时间发送过来的广播信息，为了方便设置提醒内容
        title = intent.getStringExtra("title");
        Log.i(TAG, title);
        dsc = intent.getStringExtra("dsc");
        showNormal(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, AlarmService.class);
        context.startService(intent);  //回调Service,同一个Service只会启动一个，所以直接再次启动Service，会重置开启新的提醒，
    }
    /**
     *
     *发送通知
     */
    private void showNormal(Context context) {
        Intent intent = new Intent(context, MainActivity.class);//这里是点击Notification 跳转的界面，可以自己选择
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.clock)     //设置通知图标。
                .setTicker(title)        //通知时在状态栏显示的通知内容
                .setContentInfo("事项提醒")        //内容信息
                .setContentTitle(title)        //设置通知标题。
                .setContentText(dsc)        //设置通知内容。
                .setAutoCancel(true)                //点击通知后通知消失
                .setDefaults(Notification.DEFAULT_ALL)        //设置系统默认的通知音乐、振动、LED等。
                .setContentIntent(pi)
                .build();
        manager.notify(NOTIFICATION_ID_1, notification);
    }}
