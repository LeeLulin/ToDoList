package com.example.lulin.todolist.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.Todos;

import java.util.List;


/**
 * 新建待办事项类
 * Created by Lulin on 2018/5/5.
 */
public class NewTodoActivity extends AppCompatActivity {

    private ListView mListView;
    private List<Todos> list;
    private MyDatabaseHelper dbHelper;

    private String todoTitle,todoDsc;
    private Button ok,cancel;
    private TextView et_todo_title,et_todo_dsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        initFindview();
        initView();
    }

    private void initFindview() {
        ok = (Button) findViewById(R.id.bt_new_ok);
        cancel = (Button) findViewById(R.id.bt_new_cancel);
        et_todo_title = (TextView) findViewById(R.id.new_todo_title);
        et_todo_dsc = (TextView) findViewById(R.id.new_todo_dsc);
    }

    private void initView() {

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                todoTitle = et_todo_title.getText().toString();
                todoDsc = et_todo_dsc.getText().toString();
                dbHelper = new MyDatabaseHelper(NewTodoActivity.this, "Data.db", null, 2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //插入数据
                values.put("todotitle", todoTitle);
                values.put("tododsc", todoDsc);
                db.insert("Todo", null, values); //插入第一条数据
                Intent intent = new Intent(NewTodoActivity.this, MainActivity.class);
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

    }


}
