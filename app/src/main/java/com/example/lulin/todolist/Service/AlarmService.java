package com.example.lulin.todolist.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lulin.todolist.Receiver.AlarmReceiver;
import com.example.lulin.todolist.utils.ToDoUtils;
import com.example.lulin.todolist.utils.Todos;

import java.util.List;


public class AlarmService extends Service {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent startNotification;
    private static final String TAG = "service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "服务启动！");
        List<Todos> todosList = ToDoUtils.getTodayTodos(this);
        if (todosList != null) {
            try {
                for (Todos todos : todosList) {
                    if (todos.getRemindTime() - System.currentTimeMillis() > 0) {
                        startNotification = new Intent(AlarmService.this, AlarmReceiver.class);   //启动广播
                        startNotification.putExtra("title", todos.getTitle());
                        startNotification.putExtra("dsc", todos.getDesc());
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   //这里是系统闹钟的对象
                        pendingIntent = PendingIntent.getBroadcast(this, todos.getId(), startNotification, PendingIntent.FLAG_UPDATE_CURRENT);     //设置事件
                        Log.i(TAG, "标题是:" + todos.getTitle());
                        Log.i(TAG, "时间是:" + todos.getRemindTime());
                        Log.i(TAG, "日期是:" + System.currentTimeMillis() / 1000 / 60 / 60 / 24);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, todos.getRemindTime(), pendingIntent);    //提交事件，发送给 广播接收器
                        ToDoUtils.setHasAlerted(this,todos.getId());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return START_REDELIVER_INTENT;
    } //这里为了提高优先级，选择START_REDELIVER_INTENT 没那么容易被内存清理时杀死

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}


