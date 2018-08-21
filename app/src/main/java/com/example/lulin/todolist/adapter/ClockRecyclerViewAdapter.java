package com.example.lulin.todolist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.Tomato;

import java.util.List;

/**
 * RecyclerView适配器
 */
public class ClockRecyclerViewAdapter extends RecyclerView.Adapter<ClockRecyclerViewAdapter.ViewHolder> {

    private List<Tomato> tomato;
    private Context context;


    public ClockRecyclerViewAdapter(List<Tomato> tomato, Context context) {
        this.tomato = tomato;
        this.context=context;
    }


    //自定义ViewHolder类
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView clock_title;
        TextView todo_desc;
        TextView todo_date;
        TextView todo_time;
        TextView isAlerted;
        ImageView clock_card_bg;
        TextView isRepeat;



        public ViewHolder(View itemView) {
            super(itemView);
            clock_title = (TextView) itemView.findViewById(R.id.clock_title);
//            todo_desc = (TextView) itemView.findViewById(R.id.todo_desc);
//            todo_date = (TextView) itemView.findViewById(R.id.todo_date);
//            isRepeat = (TextView) itemView.findViewById(R.id.isRepeat);
//            todo_time = (TextView) itemView.findViewById(R.id.todo_time);
//            isAlerted = (TextView) itemView.findViewById(R.id.isAlerted);
            clock_card_bg = (ImageView) itemView.findViewById(R.id.clock_card_bg);

        }


    }
    @Override
    public ClockRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_clock,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClockRecyclerViewAdapter.ViewHolder ViewHolder, int i) {

        ViewHolder.clock_title.setText(tomato.get(tomato.size()-1-i).getTitle());

//        ViewHolder.todo_desc.setText(todos.get(todos.size()-1-i).getDesc());
//        ViewHolder.todo_date.setText(todos.get(todos.size()-1-i).getDate() + " "+ todos.get(todos.size()-1-i).getTime());
        ViewHolder.clock_card_bg.setImageDrawable(context.getResources().getDrawable(tomato.get(tomato.size()-1-i).getImgId()));
//
//        if (todos.get(todos.size()-1-i).getIsRepeat() == 1){
//            ViewHolder.isRepeat.setText("重复");
//            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
//        }else {
//            ViewHolder.isRepeat.setText("单次");
//            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
//        }


    }

    @Override
    public int getItemCount() {
        return tomato.size();
    }



    public void removeItem(int position){
        tomato.remove(tomato.size()-1-position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,tomato.size()-position);
    }


}
