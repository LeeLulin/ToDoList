package com.example.lulin.todolist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.Todos;

import java.util.List;

/**
 * RecyclerView适配器
 */
public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder> {

    private List<Todos> todos;
    private Context context;


    public TodoRecyclerViewAdapter(List<Todos> todos, Context context) {
        this.todos = todos;
        this.context=context;
    }


    //自定义ViewHolder类
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView todo_title;
        TextView todo_desc;

        public ViewHolder(View itemView) {
            super(itemView);
            todo_title= (TextView) itemView.findViewById(R.id.todo_title);
            todo_desc= (TextView) itemView.findViewById(R.id.todo_desc);

        }


    }
    @Override
    public TodoRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.todo_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoRecyclerViewAdapter.ViewHolder ViewHolder, int i) {

        ViewHolder.todo_title.setText(todos.get(todos.size()-1-i).getTitle());
        ViewHolder.todo_desc.setText(todos.get(todos.size()-1-i).getDesc());

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }



    public void removeItem(int position){
        todos.remove(position);
        notifyItemRemoved(position);
    }


}