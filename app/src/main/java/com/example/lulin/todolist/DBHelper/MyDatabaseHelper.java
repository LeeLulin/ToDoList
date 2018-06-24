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
            + "remindTime long,"
            + "remindTimeNoDay long,"
            + "isAlerted int,"
            + "imgId int,"
            + "isRepeat int )";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TODO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists Todo");
        onCreate(db);
    }
}
