package com.example.lulin.todolist.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.Activity.MainActivity;
import com.example.lulin.todolist.Utils.SPUtils;

public class AlarmReceiver extends BroadcastReceiver {
    
    private static final int NOTIFICATION_ID_1 = 0x00113;
    private String title;
    private String dsc;
    private static final String TAG = "receiver";
    private static final String KEY_RINGTONE = "ring_tone";
    private static final String KEY_VIBRATE = "vibrator";
    private String ringTone;
    private String CHANNEL_ID = "todo";
    private String CHANNEL_NAME = "Todo Notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        //此处接收闹钟时间发送过来的广播信息，为了方便设置提醒内容
        ringTone = intent.getStringExtra("ringTone");
        title = intent.getStringExtra("title");
        Log.i(TAG, title);
        dsc = intent.getStringExtra("dsc");
        showNormal(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, AlarmService.class);
        context.startService(intent);  //回调Service,同一个Service只会启动一个，所以直接再次启动Service，会重置开启新的提醒，
    }
    /**
     *发送通知
     */
    private void showNormal(Context context) {
        Intent intent = new Intent(context, MainActivity.class);//这里是点击Notification 跳转的界面，可以自己选择
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.today)     //设置通知图标。
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setTicker(title)        //通知时在状态栏显示的通知内容
                    .setContentInfo("事项提醒")        //内容信息
                    .setContentTitle(title)        //设置通知标题。
                    .setContentText(dsc)        //设置通知内容。
                    .setAutoCancel(true)                //点击通知后通知消失
//                    .setDefaults(Notification.DEFAULT_ALL)        //设置系统默认的通知音乐、振动、LED等。
                    .setPriority(Notification.PRIORITY_MAX)  //设置通知为最高权限
                    .setFullScreenIntent(pi, true)
                    .setContentIntent(pi);

        if (((String) SPUtils.get(context, KEY_RINGTONE, "")).equals("")){
            builder.setDefaults(Notification.DEFAULT_ALL); //设置系统默认的通知音乐、振动
            Log.i(TAG, "默认铃声");
        } else {
            builder.setSound(Uri.parse((String) SPUtils.get(context, KEY_RINGTONE, "")));
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            Log.i(TAG, (String) SPUtils.get(context, KEY_RINGTONE, ""));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // 注册通道，注册后除非卸载再安装否则不改变
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(CHANNEL_ID);
        }
        builder.build();
        manager.notify(NOTIFICATION_ID_1, builder.build());
    }}
