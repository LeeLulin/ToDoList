package com.example.lulin.todolist.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.Todos;

import java.util.Calendar;
import java.util.List;

/**
 * 编辑待办事项类
 */
public class EditTodoActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    private String todoTitle, todoDsc,todoDate,todoTime;
    private String n_todoTitle, n_todoDsc;
    private String n_todoDate, n_todoTime;
    private Button ok, cancel;
    private TextView et_todo_title, et_todo_dsc, et_todo_date, et_todo_time;
    private int mYear,mMonth,mDay;//当前日期
    private int mHour,mMin;//当前时间
    private Calendar ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
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
        mHour = ca.get(Calendar.HOUR);
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
                ContentValues values = new ContentValues();
                //更新数据库
                values.put("todotitle", n_todoTitle);
                values.put("tododsc", n_todoDsc);
                values.put("tododate", n_todoDate);
                values.put("todotime", n_todoTime);
                db.update("Todo",values,"todotitle = ?", new String[]{todoTitle});
                Intent intent = new Intent(EditTodoActivity.this, MainActivity.class);
                startActivity(intent);
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
}