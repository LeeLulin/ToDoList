package com.example.lulin.todolist.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * Created by Lulin on 2018/5/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String TODO = "create table Todo ("
            + "id integer primary key autoincrement, "
            + "todotitle String, "
            + "tododsc String,"
            + "tododate String,"
            + "todotime String,"
            + "objectId String,"
            + "remindTime long,"
            + "remindTimeNoDay long,"
            + "isAlerted int,"
            + "imgId int,"
            + "isRepeat int )";

    public static final String CLOCK = "create table Clock ("
            + "id integer primary key autoincrement,"
            + "objectId String,"
            + "clocktitle String,"
            + "workLength int,"
            + "shortBreak int,"
            + "longBreak int,"
            + "frequency int,"
            + "imgId int )";

    public static final String TIME = "create table timer_schedule ("
            + "_id integer primary key autoincrement,"
            + "clocktitle String,"
            + "start_time DATETIME,"
            + "end_time DATETIME,"
            + "duration INTEGER,"
            + "date_add DATE)";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TODO);
        db.execSQL(CLOCK);
        db.execSQL(TIME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists Todo");
        db.execSQL("drop table if exists Clock");
        db.execSQL("drop table if exists timer_schedule");
        onCreate(db);
    }
}
