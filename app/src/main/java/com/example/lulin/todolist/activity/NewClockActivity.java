package com.example.lulin.todolist.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.Dao.ToDoDao;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.utils.Clock;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;



/**
 * 新建待办事项类
 * Created by Lulin on 2018/5/5.
 */
public class NewClockActivity extends BasicActivity {

    private MyDatabaseHelper dbHelper;
    private String clockTitle,todoDsc;
    private String todoDate = null, todoTime = null;
    private Button ok,cancel;
    private FloatingActionButton fab_ok;
    private TextView nv_clock_title,nv_todo_dsc,nv_todo_date,nv_todo_time;
    private int mYear,mMonth,mDay;//当前日期
    private int mHour,mMin;//当前时间
    private long remindTime, remindTimeNoDay;
    private Calendar ca;
    private Date data;
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
    private static final String KEY_RINGTONE = "ring_tone";
    private Clock clock;
    SQLiteDatabase db;

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
        ca = Calendar.getInstance();
        getDate();
        getTime();
        initFindview();
        initView();
//        initHeadImage();
    }

    private void initFindview() {
//        ok = (Button) findViewById(R.id.bt_new_ok);
        fab_ok = (FloatingActionButton) findViewById(R.id.fab_clock);
//        cancel = (Button) findViewById(R.id.bt_new_cancel);
//        nv_clock_title = (TextView) findViewById(R.id.new_clock_title);
//        nv_todo_dsc = (TextView) findViewById(R.id.new_clock_dsc);
//        nv_todo_date = (TextView) findViewById(R.id.new_clock_date);
//        nv_todo_time = (TextView) findViewById(R.id.new_clock_time);
        new_bg = (ImageView) findViewById(R.id.clock_card_bg);
    }

    private void initHeadImage(){

        Random random = new Random();
        imgId = imageArray[random.nextInt(8)];
        new_bg.setImageDrawable(getApplicationContext().getResources().getDrawable(imgId));

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

        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nv_clock_title = (TextView) findViewById(R.id.new_clock_title);
                clockTitle = nv_clock_title.getText().toString();
//                    todoDsc = nv_todo_dsc.getText().toString();

                ContentValues values = new ContentValues();
                values.put("clocktitle", clockTitle);
                db.insert("Clock",null,values);
                Intent intent = new Intent(NewClockActivity.this, ClockActivity.class);
                intent.putExtra("clocktitle",clockTitle);
                startActivity(intent);
                finish();

//                    Calendar calendarTime = Calendar.getInstance();
//                    calendarTime.setTimeInMillis(System.currentTimeMillis());
//                    calendarTime.set(Calendar.YEAR, mYear);
//                    calendarTime.set(Calendar.MONTH, mMonth);
//                    calendarTime.set(Calendar.DAY_OF_MONTH, mDay);
//                    calendarTime.set(Calendar.HOUR_OF_DAY, mHour);
//                    calendarTime.set(Calendar.MINUTE, mMin);
//                    calendarTime.set(Calendar.SECOND, 0);
//                    remindTime = calendarTime.getTimeInMillis();
//                    Log.i(TAG, "时间是"+String.valueOf(remindTime));
                    //插入数据
//                    values.put("todotitle", todoTitle);
//                    values.put("tododsc", todoDsc);
//                    values.put("tododate", todoDate);
//                    values.put("todotime", todoTime);
//                    values.put("remindTime", remindTime);
//                    values.put("isAlerted", 0);
//                    values.put("isRepeat", isRepeat);
//                    values.put("imgId", imgId);
//
//                    db.insert("Todo", null, values);

//                    User user = BmobUser.getCurrentUser(User.class);
//                    Date date = new Date(remindTime);
//                    BmobDate bmobDate = new BmobDate(date);


//                    boolean isSync = (Boolean) SPUtils.get(getApplication(),"sync",true);
//                    Log.i("ToDo", "isSync: " + isSync);

//                    if (isSync){
//                        //保存数据到Bmob
//                        if(NetWorkUtils.isNetworkConnected(getApplication()) && User.getCurrentUser()!= null){
//                            todos.save(new SaveListener<String>() {
//                                @Override
//                                public void done(String s, BmobException e) {
//                                    if(e==null){
//                                        //插入本地数据库
//                                        new ToDoDao(getApplicationContext()).create(todos);
//                                        Log.i("bmob","保存到Bmob成功 "+ todos.getObjectId());
//                                        Log.i("bmob","保存到本地数据库成功 "+ todos.getObjectId());
//                                        Intent intent = new Intent(NewClockActivity.this, MainActivity.class);
//                                        setResult(2, intent);
//                                        startService(new Intent(NewClockActivity.this, AlarmService.class));
//                                        finish();
//                                    }else{
//                                        Log.i("bmob","保存到Bmob失败："+e.getMessage());
//                                    }
//                                }
//                            });
//
//                        } else {
//                            ToastUtils.showShort(getApplication(),"请先登录");
//                        }
//                    } else {
//                        new ToDoDao(getApplicationContext()).create(todos);
//                        Intent intent = new Intent(NewClockActivity.this, MainActivity.class);
//                        setResult(2, intent);
//                        startService(new Intent(NewClockActivity.this, AlarmService.class));
//                        finish();
//                    }

//                }

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
