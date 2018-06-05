package com.example.lulin.todolist.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.utils.Todos;

import java.util.ArrayList;
import java.util.List;
import static android.support.constraint.Constraints.TAG;

/**
 * 数据库操作
 */


public class ToDoDao {
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ToDoDao(Context context) {
        dbHelper = new MyDatabaseHelper(context.getApplicationContext(),"Data.db", null, 2);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * 获取并返回所有未被提醒的事项
     * @return
     */
    public List<Todos> getNotAlertTodos(){
        open();
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
                    cursor.getLong(cursor.getColumnIndex("remindTimeNoDay")),
                    cursor.getInt(cursor.getColumnIndex("isAlerted")),
                    cursor.getInt(cursor.getColumnIndex("isRepeat")));
            allTodos.add(data);
        }

        cursor.close();
        close();
        return allTodos;
    }

    /**
     * 获取单个待办事项
     * @param id
     * @return
     */
    public Todos getTask(int id) {
        open();
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
                    cursor.getLong(cursor.getColumnIndex("remindTimeNoDay")),
                    cursor.getInt(cursor.getColumnIndex("isAlerted")),
                    cursor.getInt(cursor.getColumnIndex("isRepeat")));
        }
        cursor.close();
        close();
        return todos;
    }

    /**
     * 设置待办事项为已提醒
     * @param id
     */
    public void setisAlerted(int id){
        open();
        Log.i(TAG, "数据已更新");
        ContentValues values = new ContentValues();
        values.put("isAlerted", 1);
        Log.i(TAG, String.valueOf(id));
        db.update("Todo", values, "id = ?", new String[]{id + ""});
        close();

    }
}
