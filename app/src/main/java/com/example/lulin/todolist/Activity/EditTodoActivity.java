package com.example.lulin.todolist.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;

import java.util.Calendar;

/**
 * 编辑待办事项类
 */
public class EditTodoActivity extends BasicActivity {

    private MyDatabaseHelper dbHelper;

    private String todoTitle, todoDsc,todoDate,todoTime;
    private String n_todoTitle, n_todoDsc;
    private String n_todoDate, n_todoTime;
    private long n_remindTime;
    private Button ok, cancel;
    private TextView et_todo_title, et_todo_dsc, et_todo_date, et_todo_time;
    private int mYear,mMonth,mDay;//当前日期
    private int mHour,mMin;//当前时间
    private Calendar ca;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBar();
        setContentView(R.layout.activity_edit_todo);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ca = Calendar.getInstance();
        getDate();
        getTime();
        initFindview();
        initView();
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

    private void initFindview() {
        ok = (Button) findViewById(R.id.bt_edit_ok);
        cancel = (Button) findViewById(R.id.bt_edit_cancel);
        et_todo_title = (TextView) findViewById(R.id.edit_todo_title);
        et_todo_dsc = (TextView) findViewById(R.id.edit_todo_dsc);
        et_todo_date = (TextView) findViewById(R.id.edit_todo_date);
        et_todo_time = (TextView) findViewById(R.id.edit_todo_time);
    }

    private void initView() {

        todoTitle = getIntent().getStringExtra("title");
        todoDsc = getIntent().getStringExtra("dsc");
        todoDate = getIntent().getStringExtra("date");
        todoTime = getIntent().getStringExtra("time");
        et_todo_title.setText(todoTitle);
        et_todo_dsc.setText(todoDsc);
        et_todo_date.setText(todoDate);
        et_todo_time.setText(todoTime);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                n_todoTitle = et_todo_title.getText().toString();
                n_todoDsc = et_todo_dsc.getText().toString();
                n_todoDate = et_todo_date.getText().toString();
                n_todoTime = et_todo_time.getText().toString();
                dbHelper = new MyDatabaseHelper(EditTodoActivity.this, "Data.db", null, 2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.setTimeInMillis(System.currentTimeMillis());
                calendarTime.set(Calendar.YEAR, mYear);
                calendarTime.set(Calendar.MONTH, mMonth);
                calendarTime.set(Calendar.DAY_OF_MONTH, mDay);
                calendarTime.set(Calendar.HOUR_OF_DAY, mHour);
                calendarTime.set(Calendar.MINUTE, mMin);
                calendarTime.set(Calendar.SECOND, 0);
                n_remindTime = calendarTime.getTimeInMillis();
                ContentValues values = new ContentValues();
                //更新数据库
                values.put("todotitle", n_todoTitle);
                values.put("tododsc", n_todoDsc);
                values.put("tododate", n_todoDate);
                values.put("todotime", n_todoTime);
                values.put("remindTime", n_remindTime);
                values.put("isAlerted", 0);
                db.update("Todo",values,"todotitle = ?", new String[]{todoTitle});
                Intent intent = new Intent(EditTodoActivity.this, MainActivity.class);
                setResult(2, intent);
                finish();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_todo_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTodoActivity.this, onDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.setCancelable(true);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.show();
            }
        });

        et_todo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTodoActivity.this, onTimeSetListener, mHour,mMin, true);
                timePickerDialog.setCancelable(true);
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
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
            n_todoDate = year+ "年"+(monthOfYear + 1) + "月" + dayOfMonth + "日";
            et_todo_date.setText(n_todoDate);
        }
    };

    /**
     * 时间选择对话框监听
     */
    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            if (minute < 10){
                n_todoTime = hour + ":" + "0" + minute;
            } else {
                n_todoTime = hour + ":" + minute;
            }
            et_todo_time.setText(n_todoTime);
        }
    };

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