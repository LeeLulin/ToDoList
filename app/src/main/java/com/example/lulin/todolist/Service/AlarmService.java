package com.example.lulin.todolist.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.Receiver.AlarmReceiver;
import com.example.lulin.todolist.utils.Todos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AlarmService extends Service {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private long alarmTime, nowTime;
    private int id;
    private String title;
    private String dsc;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor result;
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
        dbHelper= new MyDatabaseHelper(this,"Data.db", null, 2);
        db = dbHelper.getWritableDatabase();
//        result = db.rawQuery("SELECT * FROM Todo", null);
        new getnowTime().start();
        getAlarmTime();
        return START_REDELIVER_INTENT;
    } //这里为了提高优先级，选择START_REDELIVER_INTENT 没那么容易被内存清理时杀死
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    public void getAlarmTime() {
        long alarm = 1000 * 60 * 10;
        List<Todos> todosList = getTodayTodos();
        try {
            for (Todos todos : todosList) {
                if (todos.getRemindTime() - System.currentTimeMillis() < alarm) {
                    id = todos.getId();
                    alarmTime = todos.getRemindTime();
                    title = todos.getTitle();
                    dsc = todos.getDesc();
                    startNotification = new Intent(AlarmService.this, AlarmReceiver.class);   //启动广播
                    startNotification.putExtra("title", title);
                    startNotification.putExtra("dsc", dsc);
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   //这里是系统闹钟的对象
                    pendingIntent = PendingIntent.getBroadcast(this, 0, startNotification, PendingIntent.FLAG_UPDATE_CURRENT);     //设置事件
                    Log.i(TAG, "标题是" + title);
                    Log.i(TAG, "时间是" + alarmTime);
                    Log.i(TAG, "日期是" + System.currentTimeMillis()/1000/60/60/24);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);    //提交事件，发送给 广播接收器
                    setisAlerted(id);
                }
            }
        }catch (Exception e){

        }


    }

    /**
     * 获取并返回所有未被提醒的事项
     * @return
     */
    public List<Todos> getNotAlertTodos(){
        List<Todos> allTodos = new ArrayList<Todos>();
        Cursor cursor = db.query("Todo",
                null, "isAlerted = ?", new String[] { "0" }, null, null, "remindTime");
        while (cursor.moveToNext()) {
            Todos data = new Todos(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("todotitle")),
                    cursor.getString(cursor.getColumnIndex("tododsc")),
                    cursor.getString(cursor.getColumnIndex("tododate")),
                    cursor.getString(cursor.getColumnIndex("todotime")),
                    cursor.getLong(cursor.getColumnIndex("remindTime")),
                    cursor.getInt(cursor.getColumnIndex("isAlerted")));
            allTodos.add(data);
        }

        cursor.close();
        db.close();
        return allTodos;
    }

    /**
     * 获取并返回今天未被提醒切大于当前时间的事项
     * @return
     */
    public List<Todos> getTodayTodos(){
        List<Todos> todayTodos = new ArrayList<Todos>();
        try {
            List<Todos> findAll = getNotAlertTodos();
            if (findAll != null && findAll.size()>0){
                for (Todos todos : findAll){
                    if (todos.getRemindTime() >= nowTime && isToday(todos.getRemindTime())){
                        todayTodos.add(todos);
                    }
                }
            }

        }catch (Exception e){

        }
        return todayTodos;
    }

    public Todos getTask(long time) {
        Todos todos = null;
        String sql = "SELECT * FROM Todo WHERE remindTime =  " + time;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            todos = new Todos(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("todotitle")),
                    cursor.getString(cursor.getColumnIndex("tododsc")),
                    cursor.getString(cursor.getColumnIndex("tododate")),
                    cursor.getString(cursor.getColumnIndex("todotime")),
                    cursor.getLong(cursor.getColumnIndex("remindTime")),
                    cursor.getInt(cursor.getColumnIndex("isAlerted")));
        }
        cursor.close();
        db.close();
        return todos;
    }
    public void setisAlerted(int id){
        Todos todos = getTask(alarmTime);
        todos.setisAlerted(1);
        ContentValues values = new ContentValues();
        values.put("isAlerted", todos.getisAlerted());
        if (todos != null){
            db.update("Todo",values,"id = ?",new String[]{id +""});
            db.close();
        }

    }

    private class getnowTime extends Thread{
        @Override
        public void run() {

            while (true) {
                nowTime = System.currentTimeMillis();
//                Log.i(TAG1, "系统时间是" + nowTime/1000);
//                if (time/1000 == nowTime/1000) {
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);    //提交事件，发送给 广播接收器
//                }
//                else {
//                    //当提醒时间为空的时候，关闭服务，下次添加提醒时再开启
//                    stopService(new Intent(AlarmService.this, AlarmService.class));
//                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private boolean isToday(long date){

        if (date/1000/60/60/24 == nowTime/1000/60/60/24){

            return true;
        }
        return false;
    }

}


