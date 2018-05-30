package com.example.lulin.todolist.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.utils.Todos;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 新建待办事项类
 * Created by Lulin on 2018/5/5.
 */
public class NewTodoActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private String todoTitle,todoDsc;
    private String todoDate = null, todoTime = null;
    private Button ok,cancel;
    private TextView nv_todo_title,nv_todo_dsc,nv_todo_date,nv_todo_time;
    private int mYear,mMonth,mDay;//当前日期
    private int mHour,mMin;//当前时间
    private long remindTime, remindDate;
    private Calendar ca;
    private Date data;
    private static final String TAG = "time";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBar();
        setContentView(R.layout.activity_new_todo);
        toolbar = (Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ca = Calendar.getInstance();
        getDate();
        getTime();
        initFindview();
        initView();
    }

    private void initFindview() {
        ok = (Button) findViewById(R.id.bt_new_ok);
        cancel = (Button) findViewById(R.id.bt_new_cancel);
        nv_todo_title = (TextView) findViewById(R.id.new_todo_title);
        nv_todo_dsc = (TextView) findViewById(R.id.new_todo_dsc);
        nv_todo_date = (TextView) findViewById(R.id.new_todo_date);
        nv_todo_time = (TextView) findViewById(R.id.new_todo_time);
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

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todoDate==null){
                    Snackbar.make(view, "未选择日期", Snackbar.LENGTH_LONG)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                } else if (todoTime==null) {
                    Snackbar.make(view, "未选择时间", Snackbar.LENGTH_LONG)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();

                } else {
                    todoTitle = nv_todo_title.getText().toString();
                    todoDsc = nv_todo_dsc.getText().toString();
                    dbHelper = new MyDatabaseHelper(NewTodoActivity.this, "Data.db", null, 2);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
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
                    values.put("todotitle", todoTitle);
                    values.put("tododsc", todoDsc);
                    values.put("tododate", todoDate);
                    values.put("todotime", todoTime);
                    values.put("remindTime", remindTime);
                    values.put("isAlerted", 0);
//                    values.put("remindDate", remindDate);
                    db.insert("Todo", null, values);
                    Intent intent = new Intent(NewTodoActivity.this, MainActivity.class);
                    setResult(2, intent);
                    startService(new Intent(NewTodoActivity.this, AlarmService.class));
                    finish();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
