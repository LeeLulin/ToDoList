package com.example.lulin.todolist.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.Bean.Todos;

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
     * 创建成功，返回记录的ID
     *
     * @param todos
     * @return
     */
    public long create(Todos todos) {

        open();
        ContentValues values = new ContentValues();
        values.put("todotitle", todos.getTitle());
        values.put("tododsc", todos.getDesc());
        values.put("tododate", todos.getDate());
        values.put("todotime", todos.getTime());
        values.put("remindTime", todos.getRemindTime());
        values.put("isAlerted", todos.getisAlerted());
        values.put("isRepeat", todos.getIsRepeat());
        values.put("imgId", todos.getImgId());
        values.put("objectId",todos.getObjectId());
        long id = db.insert("Todo", null, values);
        close();
        return id;
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
            Todos data = new Todos();
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("todotitle")));
            data.setDesc(cursor.getString(cursor.getColumnIndex("tododsc")));
            data.setDate(cursor.getString(cursor.getColumnIndex("tododate")));
            data.setTime(cursor.getString(cursor.getColumnIndex("todotime")));
            data.setRemindTime(cursor.getLong(cursor.getColumnIndex("remindTime")));
            data.setRemindTimeNoDay(cursor.getLong(cursor.getColumnIndex("remindTimeNoDay")));
            data.setisAlerted(cursor.getInt(cursor.getColumnIndex("isAlerted")));
            data.setIsRepeat(cursor.getInt(cursor.getColumnIndex("isRepeat")));
            data.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
            allTodos.add(data);
        }

        cursor.close();
        close();
        return allTodos;
    }

    /**
     * 获取所有task
     *
     * @return
     */
    public List<Todos> getAllTask() {
        open();
        List<Todos> todosList = new ArrayList<Todos>();
        Cursor cursor=db.rawQuery("SELECT * FROM Todo", null);
        while(cursor.moveToNext()) {
            Todos data = new Todos();
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("todotitle")));
            data.setDesc(cursor.getString(cursor.getColumnIndex("tododsc")));
            data.setDate(cursor.getString(cursor.getColumnIndex("tododate")));
            data.setTime(cursor.getString(cursor.getColumnIndex("todotime")));
            data.setRemindTime(cursor.getLong(cursor.getColumnIndex("remindTime")));
            data.setRemindTimeNoDay(cursor.getLong(cursor.getColumnIndex("remindTimeNoDay")));
            data.setisAlerted(cursor.getInt(cursor.getColumnIndex("isAlerted")));
            data.setIsRepeat(cursor.getInt(cursor.getColumnIndex("isRepeat")));
            data.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
            data.setObjectId(cursor.getString(cursor.getColumnIndex("objectId")));
            todosList.add(data);
        }
        // make sure to close the cursor

        cursor.close();
        close();
        Log.i("ToDoDao", "查询到本地的任务个数：" + todosList.size());
        return todosList;
    }

    /**
     * 获取单个待办事项
     * @param id
     * @return
     */
    public Todos getTask(int id) {
        open();
        Todos data = new Todos();
        Cursor cursor = db.rawQuery("SELECT * FROM Todo WHERE id =" + id, null);
        if (cursor.moveToNext()) {
            data.setId(cursor.getInt(cursor.getColumnIndex("id")));
            data.setTitle(cursor.getString(cursor.getColumnIndex("todotitle")));
            data.setDesc(cursor.getString(cursor.getColumnIndex("tododsc")));
            data.setDate(cursor.getString(cursor.getColumnIndex("tododate")));
            data.setTime(cursor.getString(cursor.getColumnIndex("todotime")));
            data.setRemindTime(cursor.getLong(cursor.getColumnIndex("remindTime")));
            data.setRemindTimeNoDay(cursor.getLong(cursor.getColumnIndex("remindTimeNoDay")));
            data.setisAlerted(cursor.getInt(cursor.getColumnIndex("isAlerted")));
            data.setIsRepeat(cursor.getInt(cursor.getColumnIndex("isRepeat")));
            data.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
            data.setObjectId(cursor.getString(cursor.getColumnIndex("objectId")));
        }
        cursor.close();
        close();
        return data;
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

    /**
     * 保存多个
     *
     * @param list
     */
    public void saveAll(List<Todos> list) {
        for (Todos todos : list) {
            create(todos);
        }
    }

    /**
     * 清空表数据
     */
    public void clearAll() {
        open();
        db.execSQL("delete from Todo");
        db.execSQL("update sqlite_sequence set seq = 0 where name = 'Todo' ");
        close();
    }


}
