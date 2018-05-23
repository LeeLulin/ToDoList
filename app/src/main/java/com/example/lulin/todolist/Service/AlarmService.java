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
import java.util.List;


public class AlarmService extends Service {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private long alarmTime, nowTime;
    private int id;
    private String title;
    private String dsc;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
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
        dbHelper= new MyDatabaseHelper(this,"Data.db", null, 2);
        db = dbHelper.getWritableDatabase();
        new getnowTime().start();
        getAlarmTime();
        return START_REDELIVER_INTENT;
    } //这里为了提高优先级，选择START_REDELIVER_INTENT 没那么容易被内存清理时杀死

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取即将要提醒的事项，发送系统广播
     */
    public void getAlarmTime() {
        long alarm = 1000 * 60 * 10;
        List<Todos> todosList = getTodayTodos();
        try {
            for (Todos todos : todosList) {
                if (todos.getRemindTime() - nowTime < alarm) {
//                    id = todos.getId();
//                    alarmTime = todos.getRemindTime();
//                    title = todos.getTitle();
//                    dsc = todos.getDesc();
                    startNotification = new Intent(AlarmService.this, AlarmReceiver.class);   //启动广播
                    startNotification.putExtra("title", todos.getTitle());
                    startNotification.putExtra("dsc", todos.getDesc());
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   //这里是系统闹钟的对象
                    pendingIntent = PendingIntent.getBroadcast(this, 0, startNotification, PendingIntent.FLAG_UPDATE_CURRENT);     //设置事件
                    Log.i(TAG, "标题是:" + todos.getTitle());
                    Log.i(TAG, "时间是:" + todos.getRemindTime());
                    Log.i(TAG, "日期是:" + System.currentTimeMillis()/1000/60/60/24);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, todos.getRemindTime(), pendingIntent);    //提交事件，发送给 广播接收器
//                    setisAlerted(id);

                }

            }

        }catch (Exception e){

        }

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
     * 获取单个待办事项
     * @param id
     * @return
     */
    public Todos getTask(int id) {
        Todos todos = null;
        Cursor cursor = db.rawQuery("SELECT * FROM Todo WHERE id =" + id, null);
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
//        db.close();
        return todos;
    }

    /**
     * 设置待办事项为已提醒
     * @param id
     */
    public void setisAlerted(int id){
        Log.i(TAG, "数据已更新");
        ContentValues values = new ContentValues();
        values.put("isAlerted", 1);
        Log.i(TAG, String.valueOf(id));
        db.update("Todo", values, "id = ?", new String[]{id + ""});
        db.close();

    }

    /**
     * 获取当前时间的线程
     */
    private class getnowTime extends Thread{
        @Override
        public void run() {

            while (true) {
                nowTime = System.currentTimeMillis();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 判断要提醒的事项是否为今天
     * @param date
     * @return
     */
    private boolean isToday(long date){

        if (date/1000/60/60/24 == nowTime/1000/60/60/24){

            return true;
        }
        return false;
    }

}


