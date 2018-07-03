package com.example.lulin.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.utils.Todos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ClockDBAdapter {
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

    public ClockDBAdapter(Context context) {
        mDbHelper = new MyDatabaseHelper(context,"Data.db", null, 2);
    }

    public ClockDBAdapter open() {
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

    public HashMap getAmount() {
        HashMap<String, Integer> results = new HashMap<>();

        String[] projection = {
                _ID,
                COLUMN_NAME_END_TIME,
                COLUMN_NAME_DURATION
        };

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                       // The columns to return
                null,                        // The columns for the WHERE clause
                null,                    // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort order
        );

        int duration = 0;
        int times  = 0;

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

    private static String formatDateTime(Date date) {
        return formatDateTime.format(date);
    }

    private static String formatDate(Date date) {
        return formatDate.format(date);
    }
}
