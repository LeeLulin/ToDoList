package com.example.lulin.todolist.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lulin.todolist.Bean.BaiDuVoice;
import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.Dao.ToDoDao;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Utils.ToastUtils;
import com.example.lulin.todolist.Bean.Todos;
import com.example.lulin.todolist.Bean.User;
import com.github.jorgecastilloprz.FABProgressCircle;
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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;
import me.drakeet.materialdialog.MaterialDialog;


/**
 * 新建待办事项类
 * Created by Lulin on 2018/5/5.
 */
public class NewTodoActivity extends BasicActivity implements EventListener {

    private String todoTitle,todoDsc;
    private String todoDate = null, todoTime = null;
    private FloatingActionButton fab_ok;
    private TextView nv_todo_date,nv_todo_time,voice_result;
    private EditText nv_todo_title,nv_todo_dsc;
    private Switch nv_repeat;
    private int mYear,mMonth,mDay;//当前日期
    private int mHour,mMin;//当前时间
    private long remindTime;
    private Calendar ca;
    private static final String TAG = "time";
    private Toolbar toolbar;
    private int isRepeat = 0;
    private ImageView new_bg;
    private static int[] imageArray = new int[]{R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,};
    private int imgId;
    private Todos todos;
    private FABProgressCircle fabProgressCircle;
    private MaterialDialog voice;
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
    private Button mic_title,mic_dsc;
    private boolean enableOffline = true;
    private EventManager manager;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_new_todo);
        toolbar = (Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ca = Calendar.getInstance();
        getDate();
        getTime();
        initPermission();
        initBaiduRecognizer();
        initView();
        initHeadImage();
    }

    private void initHeadImage(){

        Random random = new Random();
        imgId = imageArray[random.nextInt(8)];
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);
        Glide.with(getApplicationContext())
                .load(imgId)
                .apply(options)
                .into(new_bg);

    }
    /**
     * 获取日期
     */
    private void getDate(){

        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取时间
     */
    private void getTime(){
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMin = ca.get(Calendar.MINUTE);
    }

    private void initView() {

        fab_ok = (FloatingActionButton) findViewById(R.id.fab_ok);
        nv_todo_title = (EditText) findViewById(R.id.new_todo_title);
        nv_todo_dsc = (EditText) findViewById(R.id.new_todo_dsc);
        nv_todo_date = (TextView) findViewById(R.id.new_todo_date);
        nv_todo_time = (TextView) findViewById(R.id.new_todo_time);
        nv_repeat = (Switch) findViewById(R.id.repeat);
        new_bg = (ImageView) findViewById(R.id.new_bg);
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabProgressCircle);
        mic_title = (Button) findViewById(R.id.mic_title);
        mic_dsc = (Button) findViewById(R.id.mic_dsc);

        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todoDate==null){
                    Toasty.info(NewTodoActivity.this, "没有设置日期", Toast.LENGTH_SHORT, true).show();
                } else if (todoTime==null) {
                    Toasty.info(NewTodoActivity.this, "没有设置提醒时间", Toast.LENGTH_SHORT, true).show();

                } else {
                    fabProgressCircle.show();
                    todoTitle = nv_todo_title.getText().toString();
                    todoDsc = nv_todo_dsc.getText().toString();
                    Calendar calendarTime = Calendar.getInstance();
                    calendarTime.setTimeInMillis(System.currentTimeMillis());
                    calendarTime.set(Calendar.YEAR, mYear);
                    calendarTime.set(Calendar.MONTH, mMonth);
                    calendarTime.set(Calendar.DAY_OF_MONTH, mDay);
                    calendarTime.set(Calendar.HOUR_OF_DAY, mHour);
                    calendarTime.set(Calendar.MINUTE, mMin);
                    calendarTime.set(Calendar.SECOND, 0);
                    remindTime = calendarTime.getTimeInMillis();
                    Log.i(TAG, "时间是"+String.valueOf(remindTime));
                    //插入数据
                    User user = BmobUser.getCurrentUser(User.class);
                    todos = new Todos();
                    todos.setUser(user);
                    todos.setTitle(todoTitle);
                    todos.setDesc(todoDsc);
                    todos.setDate(todoDate);
                    todos.setTime(todoTime);
                    todos.setRemindTime(remindTime);
                    todos.setisAlerted(0);
                    todos.setIsRepeat(isRepeat);
                    todos.setImgId(imgId);
                    Date date = new Date(remindTime);
                    BmobDate bmobDate = new BmobDate(date);
                    todos.setBmobDate(bmobDate);

                    boolean isSync = (Boolean) SPUtils.get(getApplication(),"sync",true);
                    Log.i("ToDo", "isSync: " + isSync);

                    if (isSync){
                        //保存数据到Bmob
                        if(NetWorkUtils.isNetworkConnected(getApplication()) && User.getCurrentUser()!= null){
                            todos.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        //插入本地数据库
                                        new ToDoDao(getApplicationContext()).create(todos);
                                        Log.i("bmob","保存到Bmob成功 "+ todos.getObjectId());
                                        Log.i("bmob","保存到本地数据库成功 "+ todos.getObjectId());
//                                        Intent intent = new Intent(NewTodoActivity.this, MainActivity.class);
//                                        setResult(2, intent);
                                        startService(new Intent(NewTodoActivity.this, AlarmService.class));
                                        finish();
                                    }else{
                                        Log.i("bmob","保存到Bmob失败："+e.getMessage());
                                    }
                                }
                            });

                        } else {
                            Toasty.info(NewTodoActivity.this, "请先登录", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        new ToDoDao(getApplicationContext()).create(todos);
//                        Intent intent = new Intent(NewTodoActivity.this, MainActivity.class);
//                        setResult(2, intent);
                        startService(new Intent(NewTodoActivity.this, AlarmService.class));
                        finish();
                    }

                }

            }
        });


        nv_todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewTodoActivity.this, onDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(true);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.show();

            }
        });

        nv_todo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewTodoActivity.this, onTimeSetListener, mHour,mMin, true);
                timePickerDialog.setCancelable(true);
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nv_repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    isRepeat = 1;
                } else {
                    isRepeat = 0;
                }

            }
        });

        mic_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = R.id.mic_title;
                showVoiceDialog();
                start();
            }
        });

        mic_dsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = R.id.mic_dsc;
                showVoiceDialog();
                start();
            }
        });

    }

    /**
     * 日期选择器对话框监听
     */
    public DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                todoDate = year+ "年"+(monthOfYear + 1) + "月" + dayOfMonth + "日";
                nv_todo_date.setText(todoDate);
            }
        };

    /**
     * 时间选择对话框监听
     */
    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mHour = hour;
            mMin = minute;
            if (minute < 10){
                todoTime = hour + ":" + "0" + minute;
            } else {
                todoTime = hour + ":" + minute;
            }
            nv_todo_time.setText(todoTime);
        }
    };

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
                if (flag == R.id.mic_title){
                    nv_todo_title.setText(voice.best_result);
                } else if (flag == R.id.mic_dsc){
                    nv_todo_dsc.setText(voice.best_result);
                }
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
        voice = new MaterialDialog(NewTodoActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(NewTodoActivity.this);
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
