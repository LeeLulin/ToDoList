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
 * 编辑待办事项类
 */
public class EditTodoActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    private String todoTitle, todoDsc;
    private String n_todoTitle, n_todoDsc;
    private Button ok, cancel;
    private TextView et_todo_title, et_todo_dsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        initFindview();
        initView();
    }

    private void initFindview() {
        ok = (Button) findViewById(R.id.bt_edit_ok);
        cancel = (Button) findViewById(R.id.bt_edit_cancel);
        et_todo_title = (TextView) findViewById(R.id.edit_todo_title);
        et_todo_dsc = (TextView) findViewById(R.id.edit_todo_dsc);
    }

    private void initView() {

        todoTitle = getIntent().getStringExtra("title");
        todoDsc = getIntent().getStringExtra("dsc");
        et_todo_title.setText(todoTitle);
        et_todo_dsc.setText(todoDsc);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                n_todoTitle = et_todo_title.getText().toString();
                n_todoDsc = et_todo_dsc.getText().toString();
                dbHelper = new MyDatabaseHelper(EditTodoActivity.this, "Data.db", null, 2);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //更新数据库
                values.put("todotitle", n_todoTitle);
                values.put("tododsc", n_todoDsc);
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

    }

    /**
     * 返回按钮监听
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}