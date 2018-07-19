package com.example.lulin.todolist.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.utils.Clock;
import com.example.lulin.todolist.utils.Tomato;
import com.example.lulin.todolist.utils.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class ClockDao {
    static final String TABLE_NAME = "timer_schedule";
    static final String _ID = "_id";
    static final String COLUMN_NAME_START_TIME = "start_time"; // 开始时间
    static final String COLUMN_NAME_END_TIME = "end_time"; // 结束时间
    static final String COLUMN_NAME_DURATION = "duration"; // 任务时长
    static final String COLUMN_NAME_DATE_ADD = "date_add"; // 添加时间

    private static final SimpleDateFormat formatDateTime =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat formatDate =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private MyDatabaseHelper mDbHelper;
    private SQLiteDatabase db;
    private User user;
    private int allTimes = 0;
    private int allDuration = 0;

    public ClockDao(Context context) {
        mDbHelper = new MyDatabaseHelper(context,"Data.db", null, 2);
    }

    public ClockDao open() {
        db = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long insert(Date startTime, long duration, String title) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_START_TIME, formatDateTime(startTime));
        values.put(COLUMN_NAME_DURATION, duration);
        values.put(COLUMN_NAME_DATE_ADD, formatDate(new Date()));
        values.put("clocktitle", title);

        return db.insert(TABLE_NAME, null, values);
    }

    public boolean update(long id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_END_TIME, formatDateTime(new Date()));

        String selection = _ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count > 0;
    }

    public void delete(long id) {
        db.delete(TABLE_NAME, _ID + " = ?", new String[] {String.valueOf(id)});
    }

    public HashMap getToday() {
        HashMap<String, Integer> results = new HashMap<>();

        String[] projection = {
                _ID,
                COLUMN_NAME_END_TIME,
                COLUMN_NAME_DURATION
        };

        String selection = COLUMN_NAME_DATE_ADD + " = ?";
        String[] selectionArgs = { formatDate(new Date()) };

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                       // The columns to return
                selection,                        // The columns for the WHERE clause
                selectionArgs,                    // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort order
        );

        int duration = 0;
        int times = 0;

        try {
            while (cursor.moveToNext()) {
                if (!cursor.isNull(cursor.getColumnIndex(COLUMN_NAME_END_TIME))) {
                    times++;
                    duration += cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_DURATION));
                }
            }
        } finally {
            cursor.close();
        }


        results.put("times", times);
        results.put("duration", duration);

        return results;
    }

    /**
     * 保存多个
     *
     * @param list
     */
    public void saveAll(List<Tomato> list) {
        for (Tomato tomato : list) {
            create(tomato);
        }
    }

    /**
     * 创建成功，返回记录的ID
     *
     * @param tomato
     * @return
     */
    public long create(Tomato tomato) {

        open();
        ContentValues values = new ContentValues();
        values.put("clocktitle", tomato.getTitle());
        long id = db.insert("Clock", null, values);
        close();
        return id;
    }



    public List<Tomato> getDbAllTomato() {
        open();
        List<Tomato> todosList = new ArrayList<Tomato>();
        Cursor cursor=db.rawQuery("SELECT * FROM Clock", null);
        while(cursor.moveToNext()) {
            Tomato data = new Tomato();
            data.setTitle(cursor.getString(cursor.getColumnIndex("clocktitle")));
            data.setObjectId(cursor.getString(cursor.getColumnIndex("objectId")));
            todosList.add(data);
        }
        // make sure to close the cursor

        cursor.close();
        close();
        Log.i("ClockDao", "查询到本地的番茄任务个数：" + todosList.size());
        return todosList;
    }

    public HashMap getAmount() {
        HashMap<String, Integer> results = new HashMap<>();

//        String[] projection = {
//                _ID,
//                COLUMN_NAME_END_TIME,
//                COLUMN_NAME_DURATION
//        };
//
//        Cursor cursor = db.query(
//                TABLE_NAME,                     // The table to query
//                projection,                       // The columns to return
//                null,                        // The columns for the WHERE clause
//                null,                    // The values for the WHERE clause
//                null,                            // don't group the rows
//                null,                            // don't filter by row groups
//                null                             // don't sort order
//        );
//
//        int duration = 0;
//        int times  = 0;
//
//        try {
//            while (cursor.moveToNext()) {
//                if (!cursor.isNull(cursor.getColumnIndex(COLUMN_NAME_END_TIME))) {
//                    times++;
//                    duration += cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_DURATION));
//                }
//            }
//        } finally {
//            cursor.close();
//        }

        user = User.getCurrentUser(User.class);
        BmobQuery<Clock> bmobQuery = new BmobQuery<Clock>();
        bmobQuery.addWhereEqualTo("user", user);
        bmobQuery.findObjects(new FindListener<Clock>() {
            @Override
            public void done(List<Clock> list, BmobException e) {
                if (e==null){
                    Log.i("Clock", "查询到: " +list.size()+" 条数据");
                    for (Clock clock : list){
                        if (clock.getEnd_time()!=null){
                            allTimes++;
                            allDuration += clock.getDuration();
                        }
                    }
                    Log.i("Clock", "番茄钟个数：" + allTimes);
                    Log.i("Clock", "累计时间： " + allDuration);
                } else {

                }
            }
        });

        results.put("times", allTimes);
        results.put("duration", allDuration);

        return results;
    }

    public static String formatDateTime(Date date) {
        return formatDateTime.format(date);
    }

    public static String formatDate(Date date) {
        return formatDate.format(date);
    }
}
