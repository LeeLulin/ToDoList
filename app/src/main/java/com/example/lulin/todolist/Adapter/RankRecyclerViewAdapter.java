package com.example.lulin.todolist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Bean.User;

import java.util.List;
/**
 * RecyclerView适配器
 */
public class RankRecyclerViewAdapter extends RecyclerView.Adapter<RankRecyclerViewAdapter.ViewHolder> {

    private List<User> rankList;
    private Context context;
    private User user;


    public RankRecyclerViewAdapter(List<User> rankList, Context context) {
        this.rankList = rankList;
        this.context = context;
    }


    //自定义ViewHolder类
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView user_name;
        TextView total;



        public ViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.u_name);
            total = (TextView) itemView.findViewById(R.id.user_total);

        }


    }

    @Override
    public RankRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_rank,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RankRecyclerViewAdapter.ViewHolder ViewHolder, int i) {

        user = User.getCurrentUser(User.class);
        ViewHolder.user_name.setText(rankList.get(rankList.size()-1-i).getNickName());
        ViewHolder.total.setText(rankList.get(rankList.size()-1-i).getTotal().toString() + "分钟");
        if (rankList.get(rankList.size()-1-i).getNickName().equals(user.getNickName())){
            ViewHolder.user_name.setTextColor(Color.parseColor("#54aadb"));
            ViewHolder.total.setTextColor(Color.parseColor("#54aadb"));
        }


    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }


}