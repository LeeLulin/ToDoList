package com.example.lulin.todolist.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lulin.todolist.Bean.BaiDuVoice;
import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Bean.Clock;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Utils.SeekBarPreference;
import com.example.lulin.todolist.Bean.Tomato;
import com.example.lulin.todolist.Bean.User;
import com.example.lulin.todolist.Utils.ToastUtils;
import com.example.lulin.todolist.Widget.ClockApplication;
import com.google.gson.Gson;
import com.lai.library.ButtonStyle;
import com.pl.voiceAnimation.VoiceAnimator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;
import me.drakeet.materialdialog.MaterialDialog;


/**
 * 新建待办事项类
 * Created by Lulin on 2018/5/5.
 */
public class NewClockActivity extends BasicActivity implements EventListener {

    private MyDatabaseHelper dbHelper;
    private String clockTitle;
    private FloatingActionButton fab_ok;
    private EditText nv_clock_title;
    private static final String TAG = "NewClockActivity";
    private Toolbar toolbar;
    private ImageView new_bg;
    private static int[] imageArray = new int[]{R.drawable.c_img1,
            R.drawable.c_img2,
            R.drawable.c_img3,
            R.drawable.c_img4,
            R.drawable.c_img5,
            R.drawable.c_img6,
            R.drawable.c_img7,};
    private int imgId;
    private int workLength, shortBreak,longBreak,frequency;
    SQLiteDatabase db;
    private Tomato tomato;
    private long id;
    private MaterialDialog voice;
    private TextView voice_result;
    private Button mic_clock;
    private boolean enableOffline = true;
    private EventManager manager;
    private ButtonStyle done;

    private int[] data={
            0,0,0,0,1,0,0,0,18,19,
            21,18,9,9,16,20,18,11,17,13,
            17,12,16,16,20,16,5,1,0,4,
            16,17,9,16,20,11,6,16,16,11,
            6,14,16,8,5,13,13,6,2,16,
            18,12,7,13,15,13,4,1,18,15,
            7,3,14,13,6,4,12,10,15,12,
            1,1,15,20,0,3,10,1,8,17};
    private VoiceAnimator voiceAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_new_clock);
        toolbar = (Toolbar) findViewById(R.id.new_clock_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new MyDatabaseHelper(NewClockActivity.this, "Data.db", null, 2);
        db = dbHelper.getWritableDatabase();
        initPermission();
        initBaiduRecognizer();
        initView();
        initClick();
        initHeadImage();
    }

    private void initView() {
        nv_clock_title = (EditText) findViewById(R.id.new_clock_title);
        fab_ok = (FloatingActionButton) findViewById(R.id.fab_clock);
        new_bg = (ImageView) findViewById(R.id.new_clock_bg);
        mic_clock = (Button) findViewById(R.id.mic_clock);

    }

    private void initHeadImage(){

        Random random = new Random();
        imgId = imageArray[random.nextInt(7)];

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        Glide.with(this)
                .load(imgId)
                .apply(options)
                .into(new_bg);

    }

    private void initClick() {
        Resources res = getResources();
        // 工作时长
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_work_length))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_work_length_value))
                .setMax(res.getInteger(R.integer.pref_work_length_max))
                .setMin(res.getInteger(R.integer.pref_work_length_min))
                .setUnit(R.string.pref_title_time_value)
                .setProgress((int)SPUtils
                        .get(NewClockActivity.this,"pref_key_work_length", ClockApplication.DEFAULT_WORK_LENGTH))
                .build();
        // 短时休息
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_short_break))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_short_break_value))
                .setMax(res.getInteger(R.integer.pref_short_break_max))
                .setMin(res.getInteger(R.integer.pref_short_break_min))
                .setUnit(R.string.pref_title_time_value)
                .setProgress((int)SPUtils
                        .get(NewClockActivity.this,"pref_key_short_break", ClockApplication.DEFAULT_SHORT_BREAK))
                .build();
        // 长时休息
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_long_break))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_long_break_value))
                .setMax(res.getInteger(R.integer.pref_long_break_max))
                .setMin(res.getInteger(R.integer.pref_long_break_min))
                .setUnit(R.string.pref_title_time_value)
                .setProgress((int)SPUtils
                        .get(NewClockActivity.this,"pref_key_long_break", ClockApplication.DEFAULT_LONG_BREAK))
                .build();
        // 长时休息间隔
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_long_break_frequency))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_long_break_frequency_value))
                .setMax(res.getInteger(R.integer.pref_long_break_frequency_max))
                .setMin(res.getInteger(R.integer.pref_long_break_frequency_min))
                .setUnit(R.string.pref_title_frequency_value)
                .setProgress((int)SPUtils
                        .get(NewClockActivity.this,"pref_key_long_break_frequency", ClockApplication.DEFAULT_LONG_BREAK_FREQUENCY))
                .build();

        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clockTitle = nv_clock_title.getText().toString();
                workLength = (int)SPUtils
                        .get(NewClockActivity.this,"pref_key_work_length", ClockApplication.DEFAULT_WORK_LENGTH);
                shortBreak = (int)SPUtils
                        .get(NewClockActivity.this,"pref_key_short_break", ClockApplication.DEFAULT_SHORT_BREAK);
                longBreak = (int)SPUtils
                        .get(NewClockActivity.this,"pref_key_long_break", ClockApplication.DEFAULT_LONG_BREAK);
                frequency = (int)SPUtils
                        .get(NewClockActivity.this,"pref_key_long_break_frequency", ClockApplication.DEFAULT_LONG_BREAK_FREQUENCY);
                User user = User.getCurrentUser(User.class);
                tomato = new Tomato();
                tomato.setUser(user);
                tomato.setTitle(clockTitle);
                tomato.setWorkLength(workLength);
                tomato.setShortBreak(shortBreak);
                tomato.setLongBreak(longBreak);
                tomato.setFrequency(frequency);
                tomato.setImgId(imgId);
                boolean isSync = (Boolean) SPUtils.get(getApplication(),"sync",true);
                if(isSync){
                    if(NetWorkUtils.isNetworkConnected(getApplication()) && User.getCurrentUser(User.class)!= null){
                        tomato.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    ContentValues values = new ContentValues();
                                    values.put("clocktitle", clockTitle);
                                    values.put("workLength", workLength);
                                    values.put("shortBreak", shortBreak);
                                    values.put("longBreak", longBreak);
                                    values.put("frequency", frequency);
                                    values.put("objectId", tomato.getObjectId());
                                    values.put("imgId", imgId);
                                    id = db.insert("Clock",null,values);
                                    Intent intent = new Intent(NewClockActivity.this, ClockActivity.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("clocktitle",clockTitle);
                                    intent.putExtra("workLength", workLength);
                                    intent.putExtra("shortBreak", shortBreak);
                                    intent.putExtra("longBreak", longBreak);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.i(TAG, "保存到bmob云失败: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        Toasty.info(NewClockActivity.this, "请先登录", Toast.LENGTH_SHORT, true).show();
                    }

                } else {
                    ContentValues values = new ContentValues();
                    values.put("clocktitle", clockTitle);
                    values.put("workLength", workLength);
                    values.put("shortBreak", shortBreak);
                    values.put("longBreak", longBreak);
                    values.put("frequency", frequency);
                    values.put("objectId", tomato.getObjectId());
                    values.put("imgId", imgId);
                    id = db.insert("Clock",null,values);
                    Intent intent = new Intent(NewClockActivity.this, ClockActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("clocktitle",clockTitle);
                    intent.putExtra("workLength", workLength);
                    intent.putExtra("shortBreak", shortBreak);
                    intent.putExtra("longBreak", longBreak);
                    startActivity(intent);
                    finish();
                }


            }
        });

        mic_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceDialog();
                start();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*注册服务*/
    private void initBaiduRecognizer() {

        manager = EventManagerFactory.create(getApplication(), "asr");
        manager.registerListener(this);
    }



    /*启动*/
    private void start() {


        Map<String, Object> params1 = new LinkedHashMap<>();
        String event = null;
        //开启录音
        event = com.baidu.speech.asr.SpeechConstant.ASR_START;
//        params1.put(com.baidu.speech.asr.SpeechConstant.APP_NAME, "");
        //音量数据回调开启
        params1.put(com.baidu.speech.asr.SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        //活动语音检测
        params1.put(com.baidu.speech.asr.SpeechConstant.VAD, com.baidu.speech.asr.SpeechConstant.VAD_DNN);
        //开启长语音(无静音超时断句)，需要手动停止录音：ASR_START
        params1.put(com.baidu.speech.asr.SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0);
        if (enableOffline) {
            //离线在线并行策略
//            params1.put(com.baidu.speech.asr.SpeechConstant.DECODER, 2);
        }
        //开启语音音频数据回调
        params1.put(com.baidu.speech.asr.SpeechConstant.ACCEPT_AUDIO_DATA, true);
        //设置文件回调
//        params1.put(com.baidu.speech.asr.SpeechConstant.IN_FILE, Environment.getExternalStorageDirectory() + "/msc/baidu.wav");
        //保存文件
        params1.put(com.baidu.speech.asr.SpeechConstant.OUT_FILE, Environment.getExternalStorageDirectory() + "/msc/baidu.wav");

        String json = null;
        json = new JSONObject(params1).toString();
        manager.send(event, json, null, 0, 0);
        Log.e(TAG, "BaiDujson：" + json);

    }

    /*停止*/
    private void stop() {
        manager.send(com.baidu.speech.asr.SpeechConstant.ASR_STOP, null, null, 0, 0);
        voice.dismiss();
    }

    String jsonstr = "{\"results_recognition\":[\"\"],\"origin_result\":{\"corpus_no\":6475915388511426951,\"err_no\":0,\"result\":{\"word\":[\"\"]},\"sn\":\"61d9af66-f57c-4175-ae9c-aa6140e93d14_s-0\"},\"error\":0,\"best_result\":\"\",\"result_type\":\"final_result\"}";

    /*语音识别回调*/
    public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {

        switch (s) {
            case com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                //成功
                jsonstr = s1;
                Gson gson = new Gson();
                BaiDuVoice voice = gson.fromJson(jsonstr, BaiDuVoice.class);
                Log.i(TAG, "BaiDuBean:" + jsonstr + "\n=========" + voice.toString());

                nv_clock_title.setText(voice.best_result);
                voice_result.setText(voice.best_result);
                break;
            case com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_READY:
                //引擎就绪，可以说话，ui改变
                break;
            case com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                //识别结束。包含异常及错误信息都通过该方法解析json获取
                break;

        }
    }

    private void showVoiceDialog(){
        voice = new MaterialDialog(NewClockActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(NewClockActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_voice, null);
        voice_result = view.findViewById(R.id.voice_result);
        done = view.findViewById(R.id.voice_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        voiceAnimator = view.findViewById(R.id.voiceAnimator);
        voiceAnimator.setAnimationMode(VoiceAnimator.AnimationMode.ANIMATION);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    for (int aData : data) {
                        voiceAnimator.setValue((aData) / 20f);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        voice.setContentView(view);
        voice.show();
    }

    /*动态权限申请*/
    private void initPermission() {
        String permission[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> applyList = new ArrayList<>();

        for (String per : permission) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, per)) {
                applyList.add(per);
            }
        }

        String tmpList[] = new String[applyList.size()];
        if (!applyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, applyList.toArray(tmpList), 123);
        }
    }

    /**
     * 返回按钮监听
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setStatusBar(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
