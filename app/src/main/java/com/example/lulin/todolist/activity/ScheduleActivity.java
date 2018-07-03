package com.example.lulin.todolist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.lulin.todolist.R;
import com.example.lulin.todolist.database.ClockDBAdapter;

import java.util.HashMap;

public class ScheduleActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, ScheduleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

//        setToolBar();

        ClockDBAdapter adapter = new ClockDBAdapter(this);

        // 今日数据
        adapter.open();
        HashMap today = adapter.getToday();
        adapter.close();

        TextView todayDurations = (TextView)findViewById(R.id.schedule_today_durations);
        TextView todayTimes = (TextView)findViewById(R.id.schedule_today_times);

        todayDurations.setText(String.valueOf(today.get("duration")));
        todayTimes.setText(String.valueOf(today.get("times")));

        // 累计数据
        adapter.open();
        HashMap amount = adapter.getAmount();
        adapter.close();

        TextView amountDurations = (TextView)findViewById(R.id.schedule_amount_durations);
        TextView amountTimes = (TextView)findViewById(R.id.schedule_amount_times);

        amountDurations.setText(String.valueOf(amount.get("duration")));
        amountTimes.setText(String.valueOf(amount.get("times")));
    }

//    private void setToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
