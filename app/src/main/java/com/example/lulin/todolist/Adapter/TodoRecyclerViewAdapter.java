package com.example.lulin.todolist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lulin.todolist.Activity.LoginActivity;
import com.example.lulin.todolist.Activity.MainActivity;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Utils.Todos;
import com.example.lulin.todolist.Interface.ItemTouchHelperAdapter;
import com.example.lulin.todolist.Interface.OnStartDragListener;

import java.util.Collections;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * RecyclerView适配器
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 */
public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Todos> todos;
    private Context context;
    private MaterialDialog dialog;
    private int truePosition,itemPosition;


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
        View v= LayoutInflater.from(context).inflate(R.layout.item_todo,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoRecyclerViewAdapter.ViewHolder ViewHolder, int i) {

        ViewHolder.todo_title.setText(todos.get(todos.size()-1-i).getTitle());
        ViewHolder.todo_desc.setText(todos.get(todos.size()-1-i).getDesc());
        ViewHolder.todo_date.setText(todos.get(todos.size()-1-i).getDate() + " "+ todos.get(todos.size()-1-i).getTime());
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(todos, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void removeItem(int position){
        truePosition = todos.size()-1-position;
        itemPosition = position;
        popAlertDialog();
//        todos.remove(todos.size()-1-position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position,todos.size()-position);
    }

    private void popAlertDialog() {

        if (dialog == null) {

            dialog = new MaterialDialog(context);
            dialog.setMessage("确定删除？")
                    .setPositiveButton("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            todos.remove(truePosition);
                            notifyItemRemoved(itemPosition);
                            notifyItemRangeChanged(itemPosition,truePosition);
                        }
                    })
                    .setNegativeButton("否", new View.OnClickListener() {
                        public void onClick(View view) {
                            notifyItemChanged(itemPosition);
                            dialog.dismiss();
                        }
                    });

        }

        dialog.show();

    }


}