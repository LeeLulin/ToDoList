package com.example.lulin.todolist.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Activity.ClockActivity;
import com.example.lulin.todolist.Dao.ClockDao;
import com.example.lulin.todolist.Utils.Clock;
import com.example.lulin.todolist.Utils.CountDownTimer;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.Sound;
import com.example.lulin.todolist.Utils.TimeFormatUtil;
import com.example.lulin.todolist.Utils.User;
import com.example.lulin.todolist.Utils.WakeLockHelper;
import com.example.lulin.todolist.Widget.ClockApplication;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ClockService extends Service implements CountDownTimer.OnCountDownTickListener {
    public static final String ACTION_COUNTDOWN_TIMER =
            "com.example.lulin.todolist.COUNTDOWN_TIMER";
    public static final String ACTION_START = "com.example.lulin.todolist.ACTION_START";
    public static final String ACTION_PAUSE = "com.example.lulin.todolist.ACTION_PAUSE";
    public static final String ACTION_RESUME = "com.example.lulin.todolist.ACTION_RESUME";
    public static final String ACTION_STOP = "com.example.lulin.todolist.ACTION_STOP";
    public static final String ACTION_TICK = "com.example.lulin.todolist.ACTION_TICK";
    public static final String ACTION_FINISH = "com.example.lulin.todolist.ACTION_FINISH";
    public static final String ACTION_AUTO_START
            = "com.example.lulin.todolist.ACTION_AUTO_START";
    public static final String ACTION_TICK_SOUND_ON =
            "com.example.lulin.todolist.ACTION_TICK_SOUND_ON";
    public static final String ACTION_TICK_SOUND_OFF =
            "com.example.lulin.todolist.ACTION_TICK_SOUND_OFF";
    public static final String ACTION_POMODORO_MODE_ON =
            "com.example.lulin.todolist.ACTION_POMODORO_MODE_OFF";

    public static final String MILLIS_UNTIL_FINISHED = "MILLIS_UNTIL_FINISHED";
    public static final String REQUEST_ACTION = "REQUEST_ACTION";
    public static final int NOTIFICATION_ID = 1;
    public static final String WAKELOCK_ID = "tick_wakelock";

    private CountDownTimer mTimer;
    private ClockApplication mApplication;
    private WakeLockHelper mWakeLockHelper;
    private ClockDao mDBAdapter;
    private Sound mSound;
    private long mID;
    private String clockTitle;
    private Clock clock;
    private User user;

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                mWakeLockHelper.release();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                mWakeLockHelper.acquire(getApplicationContext());
            }
        }
    };

    public static Intent newIntent(Context context) {
        return new Intent(context, ClockService.class);
    }

    public ClockService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = (ClockApplication)getApplication();
        mWakeLockHelper = new WakeLockHelper(WAKELOCK_ID);
        mDBAdapter = new ClockDao(getApplicationContext());
        mSound = new Sound(getApplicationContext());
        mID = 0;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(mIntentReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clockTitle = intent.getStringExtra("clockTitle");
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_AUTO_START:
                case ACTION_START:
                    stopTimer();

                    // 自动专注
                    if (action.equals(ACTION_AUTO_START)) {
                        Intent broadcastIntent = new Intent(ACTION_COUNTDOWN_TIMER);
                        broadcastIntent.putExtra(REQUEST_ACTION, ACTION_AUTO_START);
                        sendBroadcast(broadcastIntent);

                        mApplication.start();
                    }

                    long millsInTotal = getMillsInTotal();
                    mTimer = new CountDownTimer(millsInTotal);
                    mTimer.setOnChronometerTickListener(this);
                    mTimer.start();

                    mSound.play();

                    startForeground(NOTIFICATION_ID, getNotification(
                            getNotificationTitle(), formatTime(millsInTotal)).build());

                    if (mApplication.getScene() == ClockApplication.SCENE_WORK) {
                        // 插入数据
                        mDBAdapter.open();
                        mID = mDBAdapter.insert(mTimer.getStartTime(),
                                mTimer.getMinutesInFuture(),clockTitle);
                        if(NetWorkUtils.isNetworkConnected(getApplication())) {
                            user = User.getCurrentUser(User.class);
                        }
                        clock = new Clock();
                        clock.setUser(user);
                        clock.setTitle(clockTitle);
                        clock.setStart_time(ClockDao.formatDateTime(mTimer.getStartTime()));
                        clock.setDuration(mTimer.getMinutesInFuture());
                        clock.setDate_add(ClockDao.formatDate(new Date()));
                        mDBAdapter.close();
                    }
                    break;
                case ACTION_PAUSE:
                    if (mTimer != null) {
                        mTimer.pause();

                        String text = getResources().getString(R.string.notification_time_left)
                                + " " + intent.getStringExtra("time_left");

                        getNotificationManager().notify(NOTIFICATION_ID, getNotification(
                                getNotificationTitle(), text).build());
                    }

                    mSound.pause();
                    break;
                case ACTION_RESUME:
                    if (mTimer != null) {
                        mTimer.resume();
                    }

                    mSound.resume();
                    break;
                case ACTION_STOP:
                    mDBAdapter.open();
                    mDBAdapter.delete(mID);
                    mDBAdapter.close();
                    stopTimer();
                    mSound.stop();
                    break;
                case ACTION_TICK_SOUND_ON:
                    if (mTimer != null && mTimer.isRunning()) {
                        mSound.play();
                    }
                    break;
                case ACTION_TICK_SOUND_OFF:
                    mSound.stop();
                    break;
                case ACTION_POMODORO_MODE_ON:
                    // 如果处于暂停状态，但番茄模式设置为 on ,停止番茄时钟
                    if (mApplication.getState() == ClockApplication.STATE_PAUSE) {
                        if (mTimer != null) {
                            mTimer.resume();
                            mSound.resume();
                            mApplication.resume();
                        }
                    }

                    break;
            }
        }

        return START_STICKY;
    }

    private void stopTimer() {
        if (isNotificationOn()) {
            cancelNotification();
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            stopForeground(true);
        }

        mWakeLockHelper.release();
    }

    private long getMillsInTotal() {
        int minutes = mApplication.getMinutesInTotal();
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        mApplication.setMillisUntilFinished(millisUntilFinished);

        Intent intent = new Intent(ACTION_COUNTDOWN_TIMER);
        intent.putExtra(MILLIS_UNTIL_FINISHED, millisUntilFinished);
        intent.putExtra(REQUEST_ACTION, ACTION_TICK);
        sendBroadcast(intent);

        NotificationCompat.Builder builder =
                getNotification(getNotificationTitle(), formatTime(millisUntilFinished));

        getNotificationManager().notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onCountDownFinish() {
        Intent intent = new Intent(ACTION_COUNTDOWN_TIMER);
        intent.putExtra(REQUEST_ACTION, ACTION_FINISH);
        sendBroadcast(intent);

        NotificationCompat.Builder builder;

        if (mApplication.getScene() == ClockApplication.SCENE_WORK) {
            builder = getNotification(
                    getResources().getString(R.string.notification_finish),
                    getResources().getString(R.string.notification_finish_content)
            );

            if (mID > 0) {
                // 更新数据
                mDBAdapter.open();
                boolean success = mDBAdapter.update(mID);
                mDBAdapter.close();
                clock.setEnd_time(ClockDao.formatDateTime(new Date()));

                if(NetWorkUtils.isNetworkConnected(getApplication()) || User.getCurrentUser()!= null){
                clock.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            Log.i("ClockService", "保存番茄钟到bmob成功");
                            user.increment("total", getSharedPreferences()
                                    .getInt("pref_key_work_length", ClockApplication.DEFAULT_WORK_LENGTH));
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Log.i("ClockService", "番茄钟累计时间增加成功");
                                    } else {
                                        Log.i("ClockService", "番茄钟累计时间增加失败");
                                    }
                                }
                            });
                        } else {
                            Log.i("ClockService", "保存番茄钟到bmob失败: " + e.getMessage());
                        }
                    }
                });
                }

                if (success) {
                    long amountDurations =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .getLong("pref_key_amount_durations", 0);

                    SharedPreferences.Editor editor =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                    .edit();
                    amountDurations += mTimer.getMinutesInFuture();
                    editor.putLong("pref_key_amount_durations", amountDurations);
                    editor.apply();
                }
            }
        } else {
            builder = getNotification(
                    getResources().getString(R.string.notification_break_finish),
                    getResources().getString(R.string.notification_break_finish_content)
            );
        }

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("pref_key_use_notification", true)) {

            int defaults = Notification.DEFAULT_LIGHTS;

            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getBoolean("pref_key_notification_sound", true)) {
                // 结束提示音
                Uri uri = Uri.parse(
                        "android.resource://" + getPackageName() + "/" +
                                (mApplication.getScene() == ClockApplication.SCENE_WORK ?
                                        R.raw.workend :
                                        R.raw.breakend));

                builder.setSound(uri);
            }

            if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getBoolean("pref_key_notification_vibrate", true)) {
                defaults |= Notification.DEFAULT_VIBRATE;
                builder.setVibrate(new long[] {0, 1000});
            }

            builder.setDefaults(defaults);
        }

        builder.setLights(Color.GREEN, 1000, 1000);

        getNotificationManager().notify(NOTIFICATION_ID, builder.build());

        mApplication.finish();
        mSound.stop();

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("pref_key_infinity_mode", false)) {
//            Intent i = ClockService.newIntent(getApplicationContext());
            Intent i = new Intent(getBaseContext(),ClockActivity.class);
            i.setAction(ACTION_AUTO_START);

            PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, i, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 2000, pi);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
        mSound.release();
        unregisterReceiver(mIntentReceiver);
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private boolean isNotificationOn() {
        Intent intent = ClockActivity.newIntent(getApplicationContext());
        PendingIntent pi = PendingIntent
                .getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void cancelNotification() {
        Intent intent = ClockActivity.newIntent(getApplicationContext());
        PendingIntent pi = PendingIntent
                .getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE);
        pi.cancel();
        getNotificationManager().cancel(NOTIFICATION_ID);
    }

    private NotificationCompat.Builder getNotification(String title, String text) {
//        Intent intent = ClockActivity.newIntent(getApplicationContext());
        Intent intent = new Intent(getBaseContext(),ClockActivity.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.clock);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        builder.setContentText(text);

        return builder;
    }

    private String getNotificationTitle() {
        int scene = mApplication.getScene();
        int state = mApplication.getState();
        String title;

        switch (scene) {
            case ClockApplication.SCENE_WORK:
                if (state == ClockApplication.STATE_PAUSE) {
                    title = getResources().getString(R.string.notification_focus_pause);
                } else {
                    title = getResources().getString(R.string.notification_focus);
                }
                break;
            default:
                if (state == ClockApplication.STATE_PAUSE) {
                    title = getResources().getString(R.string.notification_break_pause);
                } else {
                    title = getResources().getString(R.string.notification_break);
                }
                break;
        }

        return title;
    }

    private String formatTime(long millisUntilFinished) {
        return getResources().getString(R.string.notification_time_left)  + " " +
                TimeFormatUtil.formatTime(millisUntilFinished);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }
}
