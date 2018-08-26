package com.example.lulin.todolist.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;


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
    private EditText nv_clock_title;
    private TextView nv_clock_length,nv_short_break,nv_long_break,nv_break_frequency;
    private Calendar ca;
    private Date data;
    private static final String TAG = "NewClockActivity";
    private Toolbar toolbar;
    private int isRepeat = 0;
    private ImageView new_bg;
    private static int[] imageArray = new int[]{R.drawable.c_img1,
            R.drawable.c_img2,
            R.drawable.c_img3,
            R.drawable.c_img4,
            R.drawable.c_img5,
            R.drawable.c_img6,
            R.drawable.c_img7,};
    private int imgId;
    private static final String KEY_RINGTONE = "ring_tone";
    private Clock clock;
    SQLiteDatabase db;
    private Tomato tomato;

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
        initFindview();
        initClick();
        initHeadImage();
    }

    private void initFindview() {
        fab_ok = (FloatingActionButton) findViewById(R.id.fab_clock);
        new_bg = (ImageView) findViewById(R.id.new_clock_bg);
//        nv_clock_length = (TextView) findViewById(R.id.clock_length);
//        nv_short_break = (TextView) findViewById(R.id.short_break);
//        nv_long_break = (TextView) findViewById(R.id.long_break);
//        nv_break_frequency = (TextView) findViewById(R.id.break_frequency);

    }

    private void initHeadImage(){

        Random random = new Random();
        imgId = imageArray[random.nextInt(7)];

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
                .setProgress(PreferenceManager.getDefaultSharedPreferences(this)
                        .getInt("pref_key_work_length", ClockApplication.DEFAULT_WORK_LENGTH))
                .build();
        // 短时休息
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_short_break))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_short_break_value))
                .setMax(res.getInteger(R.integer.pref_short_break_max))
                .setMin(res.getInteger(R.integer.pref_short_break_min))
                .setUnit(R.string.pref_title_time_value)
                .setProgress(PreferenceManager.getDefaultSharedPreferences(this)
                        .getInt("pref_key_short_break", ClockApplication.DEFAULT_SHORT_BREAK))
                .build();
        // 长时休息
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_long_break))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_long_break_value))
                .setMax(res.getInteger(R.integer.pref_long_break_max))
                .setMin(res.getInteger(R.integer.pref_long_break_min))
                .setUnit(R.string.pref_title_time_value)
                .setProgress(PreferenceManager.getDefaultSharedPreferences(this)
                        .getInt("pref_key_long_break", ClockApplication.DEFAULT_LONG_BREAK))
                .build();
        // 长时休息间隔
        (new SeekBarPreference(this))
                .setSeekBar((SeekBar)findViewById(R.id.pref_key_long_break_frequency))
                .setSeekBarValue((TextView)findViewById(R.id.pref_key_long_break_frequency_value))
                .setMax(res.getInteger(R.integer.pref_long_break_frequency_max))
                .setMin(res.getInteger(R.integer.pref_long_break_frequency_min))
                .setUnit(R.string.pref_title_frequency_value)
                .setProgress(PreferenceManager.getDefaultSharedPreferences(this)
                        .getInt("pref_key_long_break_frequency",
                                ClockApplication.DEFAULT_LONG_BREAK_FREQUENCY))
                .build();

        fab_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nv_clock_title = (EditText) findViewById(R.id.new_clock_title);
                clockTitle = nv_clock_title.getText().toString();
//                    todoDsc = nv_todo_dsc.getText().toString();
                User user = User.getCurrentUser(User.class);
                tomato = new Tomato();
                tomato.setUser(user);
                tomato.setTitle(clockTitle);
                tomato.setImgId(imgId);
                boolean isSync = (Boolean) SPUtils.get(getApplication(),"sync",true);
                if(isSync){
                    if(NetWorkUtils.isNetworkConnected(getApplication()) && User.getCurrentUser()!= null){
                        tomato.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    ContentValues values = new ContentValues();
                                    values.put("clocktitle", clockTitle);
                                    values.put("objectId", tomato.getObjectId());
                                    values.put("imgId", imgId);
                                    db.insert("Clock",null,values);
                                    Intent intent = new Intent(NewClockActivity.this, ClockActivity.class);
                                    intent.putExtra("clocktitle",clockTitle);
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
                    values.put("objectId", tomato.getObjectId());
                    values.put("imgId", imgId);
                    db.insert("Clock",null,values);
                    Intent intent = new Intent(NewClockActivity.this, ClockActivity.class);
                    intent.putExtra("clocktitle",clockTitle);
                    startActivity(intent);
                    finish();
                }


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
