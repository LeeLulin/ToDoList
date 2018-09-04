package com.example.lulin.todolist.Utils;

import android.content.Context;
import android.util.Log;

import com.example.lulin.todolist.Bean.Todos;
import com.example.lulin.todolist.Bean.User;
import com.example.lulin.todolist.Dao.ToDoDao;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 用于待办事项提醒服务的工具类
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
     * 返回数据库用户所有的任务
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static List<Todos> getAllTodos(Context context) {
//        DbUtils dbUtils = DbUtils.create(context);
        List<Todos> temp = new ArrayList<Todos>();
//        try {
        // temp = dbUtils.findAll(Tasks.class);
//            List<Task> findAll = dbUtils.findAll(Selector.from(Task.class).orderBy("time"));
        List<Todos> findAll = new ToDoDao(context).getAllTask();
        Log.i("ToDoUtils","任务个数" + findAll.size());
        if (findAll != null && findAll.size() > 0) {
            temp.addAll(findAll);
        }
//        } catch (Exception e) {
        // if (debugDB) e.printStackTrace();
//        }
        return temp;
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

    /**
     * 返回网络上用户所有的任务
     *
     * @param context
     * @param currentUser
     * @throws Exception
     */
    public static void getNetAllTodos(final Context context, User currentUser, final GetTodosCallBack getTodosCallBack) {
        BmobQuery<Todos> query = new BmobQuery<Todos>();
        query.addWhereEqualTo("user", currentUser).order("bmobDate");
        // 这个查询也包括了用户的已经过时的任务
        query.findObjects(new FindListener<Todos>() {
            @Override
            public void done(List<Todos> list, BmobException e) {
                if (e==null){
                    Log.i("ToDoUtils", "查询到网络任务个数: " + list.size());
                    // 1.更新本地数据库
                    if (list.size() > 0) {
//                        ToDoDao toDoDao = new ToDoDao(context);
//                        toDoDao.saveAll(list);
                        // 2.筛选大于当后时间的
                        List<Todos> listTodo = new ArrayList<Todos>();
                        long curTime = System.currentTimeMillis();
                        for (Todos todos : listTodo) {
                            if (todos.getRemindTime() >= curTime) {
                                listTodo.add(todos);
                            }
                        }
                    }
                    getTodosCallBack.onSuccess(list);

                } else {
                    Log.i("ToDoUtils", "查询失败："+e.getMessage());
                    getTodosCallBack.onError(e.getErrorCode(),e.getMessage());
                }

            }
        });
    }

    public static void deleteNetTodos(final Context context, Todos todos, final DeleteTaskListener deleteTaskListener) {
        todos.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    deleteTaskListener.onSuccess();
                } else {
                    deleteTaskListener.onError(e.getErrorCode(),e.getMessage());
                }
            }
        });
    }

    public interface GetTodosCallBack {
        void onSuccess(List<Todos> todos);

        void onError(int errorCode, String msg);
    }

    public interface DeleteTaskListener {
        void onSuccess();

        void onError(int errorCord, String msg);
    }

}
