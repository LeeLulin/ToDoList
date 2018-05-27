package com.example.lulin.todolist.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.Dao.ToDoDao;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * 待办事项提醒工具类
 */
public class ToDoUtils {

    /**
     * 获取并返回今天未被提醒切大于当前时间的事项
     * @return
     */
    public static List<Todos> getTodayTodos(Context context){
        List<Todos> todayTodos = new ArrayList<Todos>();
        try {
            List<Todos> findAll = new ToDoDao(context).getNotAlertTodos();
            if (findAll != null && findAll.size()>0){
                for (Todos todos : findAll){
                    if (todos.getRemindTime() >= System.currentTimeMillis() && isToday(todos.getRemindTime())){
                        todayTodos.add(todos);
                    }
                }
            }

        }catch (Exception e){

        }
        return todayTodos;
    }

    /**
     * 将改任务设置为已被提醒
     */
    public static void setHasAlerted(Context context, int id) {
        ToDoDao toDoDao = new ToDoDao(context);
        Todos todos = toDoDao.getTask(id);
        if (todos != null) {
            toDoDao.setisAlerted(id);
        }
    }




    /**
     * 判断要提醒的事项是否为今天
     * @param date
     * @return
     */
    private static boolean isToday(long date){

        if (date/1000/60/60/24 == System.currentTimeMillis()/1000/60/60/24){

            return true;
        }
        return false;
    }


}
