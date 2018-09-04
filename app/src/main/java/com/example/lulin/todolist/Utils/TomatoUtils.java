package com.example.lulin.todolist.Utils;

import android.content.Context;
import android.util.Log;

import com.example.lulin.todolist.Bean.Tomato;
import com.example.lulin.todolist.Bean.User;
import com.example.lulin.todolist.Dao.ClockDao;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class TomatoUtils {

    /**
     * 返回数据库用户所有的番茄钟
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static List<Tomato> getAllTomato(Context context) {
        List<Tomato> temp = new ArrayList<Tomato>();
        List<Tomato> findAll = new ClockDao(context).getDbAllTomato();
        Log.i("ClockDao","番茄任务个数" + findAll.size());
        if (findAll != null && findAll.size() > 0) {
            temp.addAll(findAll);
        }
        return temp;
    }

    /**
     * 返回网络上用户所有的番茄钟列表
     *
     * @param context
     * @param currentUser
     * @throws Exception
     */
    public static void getNetAllTomato(final Context context, User currentUser, final GetTomatoCallBack getTomatoCallBack ) {
        BmobQuery<Tomato> query = new BmobQuery<Tomato>();
        query.addWhereEqualTo("user", currentUser).order("createdAt");
        query.findObjects(new FindListener<Tomato>() {
            @Override
            public void done(List<Tomato> list, BmobException e) {
                if (e==null){
                    Log.i("TomatoUtils", "查询到网络任务个数: " + list.size());
                    // 1.更新本地数据库
//                    if (list.size() > 0) {
//                        ClockDao clockDao = new ClockDao(context);
//                        clockDao.saveAll(list);
//                    }
                    getTomatoCallBack.onSuccess(list);

                } else {
                    Log.i("TomatoUtils", "查询失败："+e.getMessage());
                    getTomatoCallBack.onError(e.getErrorCode(),e.getMessage());
                }

            }
        });
    }

    public static void deleteNetTomato(final Context context, Tomato tomato, final DeleteTomatoListener deleteTomatoListener) {
        tomato.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    deleteTomatoListener.onSuccess();
                } else {
                    deleteTomatoListener.onError(e.getErrorCode(),e.getMessage());
                }
            }
        });
    }

    public interface GetTomatoCallBack {
        void onSuccess(List<Tomato> tomato);

        void onError(int errorCode, String msg);
    }

    public interface DeleteTomatoListener {
        void onSuccess();

        void onError(int errorCord, String msg);
    }
}
