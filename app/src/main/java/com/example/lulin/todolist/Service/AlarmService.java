package com.example.lulin.todolist.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.carmelo.library.KeepLiveManager;
import com.carmelo.library.KeepliveService;
import com.example.lulin.todolist.Receiver.AlarmReceiver;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Utils.ToDoUtils;
import com.example.lulin.todolist.Bean.Todos;

import java.util.Calendar;
import java.util.List;


public class AlarmService extends KeepliveService {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent startNotification;
    private static final String TAG = "service";
    private static final String KEY_RINGTONE = "ring_tone";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "服务启动！");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTimeInMillis(System.currentTimeMillis());

        List<Todos> todosList = ToDoUtils.getTodayTodos(this);
        if (todosList != null) {
            try {
                for (Todos todos : todosList) {
                    if (todos.getRemindTime() - System.currentTimeMillis() > 0 ) {
                        startNotification = new Intent(AlarmService.this, AlarmReceiver.class);   //启动广播
                        startNotification.putExtra("title", todos.getTitle());
                        startNotification.putExtra("dsc", todos.getDesc());
                        startNotification.putExtra("ringTone", (String) SPUtils.get(getApplication(), KEY_RINGTONE, ""));
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   //这里是系统闹钟的对象
                        pendingIntent = PendingIntent.getBroadcast(this, todos.getId(), startNotification, PendingIntent.FLAG_UPDATE_CURRENT);   //设置事件
                        if (todos.getIsRepeat() == 0){
                            alarmManager.set(AlarmManager.RTC_WAKEUP, todos.getRemindTime(), pendingIntent);    //提交事件，发送给 广播接收器,提醒一次
                            Log.i(TAG, "发送单次提醒");
                            Log.i(TAG, "标题是:" + todos.getTitle());
                            Log.i(TAG, "时间是:" + todos.getRemindTime());
                            Log.i(TAG, "日期是:" + System.currentTimeMillis() / 1000 / 60 / 60 / 24);
                            Log.i(TAG, "铃声：" + (String) SPUtils.get(getApplication(), KEY_RINGTONE, ""));
                        }else if (todos.getIsRepeat() == 1){
                            //设置每隔24小时提醒一次
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, todos.getRemindTimeNoDay(), 1000 * 60 * 60 * 24, pendingIntent);
                            Log.i(TAG, "发送重复提醒");
                            Log.i(TAG, "标题是:" + todos.getTitle());
                            Log.i(TAG, "时间是:" + todos.getRemindTimeNoDay());
                            Log.i(TAG, "日期是:" + System.currentTimeMillis() / 1000 / 60 / 60 / 24);
                        }

                        ToDoUtils.setHasAlerted(this,todos.getId());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        return START_REDELIVER_INTENT;
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}


