package com.example.lulin.todolist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Switch;
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
        TextView todo_date;
        TextView todo_time;
        TextView isAlerted;
        ImageView card_background;
        TextView isRepeat;



        public ViewHolder(View itemView) {
            super(itemView);
            todo_title = (TextView) itemView.findViewById(R.id.todo_title);
            todo_desc = (TextView) itemView.findViewById(R.id.todo_desc);
            todo_date = (TextView) itemView.findViewById(R.id.todo_date);
            isRepeat = (TextView) itemView.findViewById(R.id.isRepeat);
//            todo_time = (TextView) itemView.findViewById(R.id.todo_time);
//            isAlerted = (TextView) itemView.findViewById(R.id.isAlerted);
            card_background = (ImageView) itemView.findViewById(R.id.card_bg);

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
        ViewHolder.todo_date.setText(todos.get(todos.size()-1-i).getDate().substring(5) + " "+ todos.get(todos.size()-1-i).getTime());
        ViewHolder.card_background.setImageDrawable(context.getResources().getDrawable(todos.get(todos.size()-1-i).getImgId()));

        if (todos.get(todos.size()-1-i).getIsRepeat() == 1){
            ViewHolder.isRepeat.setText("重复");
            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }else {
            ViewHolder.isRepeat.setText("单次");
            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }


    }

    @Override
    public int getItemCount() {
        return todos.size();
    }



    public void removeItem(int position){
        todos.remove(todos.size()-1-position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,todos.size()-position);
    }


}