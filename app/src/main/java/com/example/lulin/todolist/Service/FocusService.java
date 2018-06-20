package com.example.lulin.todolist.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.lulin.todolist.activity.MainActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FocusService extends Service {

    boolean flag = true;// 用于停止线程
    private ActivityManager activityManager;
    private Timer timer;
    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (activityManager == null) {
                activityManager = (ActivityManager) FocusService.this
                        .getSystemService(ACTIVITY_SERVICE);
            }

            List<ActivityManager.RecentTaskInfo> recentTasks = activityManager.getRecentTasks(
                    2, ActivityManager.RECENT_WITH_EXCLUDED);
            ActivityManager.RecentTaskInfo recentInfo = recentTasks.get(0);
            Intent intent = recentInfo.baseIntent;
            String recentTaskName = intent.getComponent().getPackageName();

//
//          ||!recentTaskName.equals("com.android.contacts")
//          ||!recentTaskName.equals("com.android.phone")
//          ||!recentTaskName.equals("com.android.launcher")
//          ||!recentTaskName.equals("com.miui.home")

            if (!recentTaskName.equals("com.example.lulin.todolist")
                    ) {
                Log.i("FocusService", "阻止运行 " + recentTaskName);
                Intent intentNewActivity = new Intent(FocusService.this, MainActivity.class);
                intentNewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentNewActivity);

            }else{
                Log.i("FocusService", "不阻止运行 "+recentTaskName);



            }
        }


    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (flag == true) {
            timer = new Timer();
            timer.schedule(task, 0, 100);
            flag = false;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
