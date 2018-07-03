package com.example.lulin.todolist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.Clock;
import java.util.List;

/**
 * RecyclerView适配器
 */
public class ClockRecyclerViewAdapter extends RecyclerView.Adapter<ClockRecyclerViewAdapter.ViewHolder> {

    private List<Clock> clock;
    private Context context;


    public ClockRecyclerViewAdapter(List<Clock> clock, Context context) {
        this.clock = clock;
        this.context=context;
    }


    //自定义ViewHolder类
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView clock_title;
        TextView todo_desc;
        TextView todo_date;
        TextView todo_time;
        TextView isAlerted;
        ImageView card_background;
        TextView isRepeat;



        public ViewHolder(View itemView) {
            super(itemView);
            clock_title = (TextView) itemView.findViewById(R.id.clock_title);
//            todo_desc = (TextView) itemView.findViewById(R.id.todo_desc);
//            todo_date = (TextView) itemView.findViewById(R.id.todo_date);
//            isRepeat = (TextView) itemView.findViewById(R.id.isRepeat);
//            todo_time = (TextView) itemView.findViewById(R.id.todo_time);
//            isAlerted = (TextView) itemView.findViewById(R.id.isAlerted);
//            card_background = (ImageView) itemView.findViewById(R.id.card_bg);

        }


    }
    @Override
    public ClockRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.clock_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClockRecyclerViewAdapter.ViewHolder ViewHolder, int i) {

        ViewHolder.clock_title.setText(clock.get(clock.size()-1-i).getTitle());

//        ViewHolder.todo_desc.setText(todos.get(todos.size()-1-i).getDesc());
//        ViewHolder.todo_date.setText(todos.get(todos.size()-1-i).getDate() + " "+ todos.get(todos.size()-1-i).getTime());
//        ViewHolder.card_background.setImageDrawable(context.getResources().getDrawable(todos.get(todos.size()-1-i).getImgId()));
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
        return clock.size();
    }



    public void removeItem(int position){
        clock.remove(clock.size()-1-position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,clock.size()-position);
    }


}
