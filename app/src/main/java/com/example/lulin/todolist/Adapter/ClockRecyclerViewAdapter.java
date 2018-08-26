package com.example.lulin.todolist.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lulin.todolist.Bean.Todos;
import com.example.lulin.todolist.Bean.User;
import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.Interface.ItemTouchHelperAdapter;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Bean.Tomato;
import com.example.lulin.todolist.Utils.ToDoUtils;
import com.example.lulin.todolist.Utils.ToastUtils;
import com.example.lulin.todolist.Utils.TomatoUtils;

import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * RecyclerView适配器
 */
public class ClockRecyclerViewAdapter extends RecyclerView.Adapter<ClockRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{

    private List<Tomato> tomatoList;
    private Context context;
    private MaterialDialog dialog;
    private int truePosition,itemPosition;
    private MyDatabaseHelper dbHelper;


    public ClockRecyclerViewAdapter(List<Tomato> tomato, Context context) {
        this.tomatoList = tomato;
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

        ViewHolder.clock_title.setText(tomatoList.get(tomatoList.size()-1-i).getTitle());

//        try{
            ViewHolder.clock_card_bg.setImageDrawable(context.getResources().getDrawable(tomatoList.get(tomatoList.size()-1-i).getImgId()));
            Log.i("load", String.valueOf(tomatoList.get(tomatoList.size()-1-i).getImgId()));
//        } catch (Exception e){
//            e.printStackTrace();
//        }



    }

    @Override
    public int getItemCount() {
        return tomatoList.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(tomatoList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition,toPosition);
        return true;
    }

    public void removeItem(int position){
        truePosition = tomatoList.size()-1-position;
        itemPosition = position;
        popAlertDialog();
//        tomatoList.remove(tomatoList.size()-1-position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, tomatoList.size()-position);
    }

    private void popAlertDialog() {

        if (dialog == null) {

            dialog = new MaterialDialog(context);
            dialog.setMessage("确定删除？")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Tomato tomato = tomatoList.get(truePosition);
                            String clockTitle = tomatoList.get(truePosition).getTitle();
                            dbHelper = new MyDatabaseHelper(context, "Data.db", null, 2);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.delete("Clock","clocktitle = ?",
                                    new String[]{clockTitle});
//                                new ToDoDao(getContext()).deleteTask(todos);
//                                todos.delete(new UpdateListener() {
//                                    @Override
//                                    public void done(BmobException e) {
//                                        if (e==null){
//                                            todoRecyclerViewAdapter.removeItem(position);
//                                        } else {
//                                            ToastUtils.showShort(getContext(),e.getMessage());
//                                        }
//                                    }
//                                });

                            if (User.getCurrentUser(User.class) != null){
                                TomatoUtils.deleteNetTomato(context, tomato, new TomatoUtils.DeleteTomatoListener() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(int errorCord, String msg) {
                                        Log.i("ClockFragment", "msg ");
                                        Toasty.warning(context, msg, Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                            tomatoList.remove(truePosition);
                            notifyItemRemoved(itemPosition);
                            notifyItemRangeChanged(itemPosition,tomatoList.size());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        public void onClick(View view) {
                            notifyItemChanged(itemPosition);
                            Log.i("sx", "item已刷新 ");
                            dialog.dismiss();
                        }
                    });

        }

        dialog.show();

    }


}
