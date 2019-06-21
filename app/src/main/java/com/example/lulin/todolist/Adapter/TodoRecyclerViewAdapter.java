package com.example.lulin.todolist.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lulin.todolist.Activity.MainActivity;
import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Utils.BitmapUtils;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Utils.ToDoUtils;
import com.example.lulin.todolist.Utils.ToastUtils;
import com.example.lulin.todolist.Bean.Todos;
import com.example.lulin.todolist.Interface.ItemTouchHelperAdapter;
import com.example.lulin.todolist.Bean.User;
import com.github.vipulasri.timelineview.TimelineView;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.drakeet.materialdialog.MaterialDialog;


public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Todos> todosList;
    private Context context;
    private MaterialDialog dialog;
    private int truePosition,itemPosition;
    private MyDatabaseHelper dbHelper;


    public TodoRecyclerViewAdapter(List<Todos> todos, Context context) {
        this.todosList = todos;
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
        TimelineView timelineView;



        public ViewHolder(View itemView) {
            super(itemView);
            todo_title = (TextView) itemView.findViewById(R.id.todo_title);
            todo_desc = (TextView) itemView.findViewById(R.id.todo_desc);
            todo_date = (TextView) itemView.findViewById(R.id.todo_date);
            isRepeat = (TextView) itemView.findViewById(R.id.isRepeat);
//            todo_time = (TextView) itemView.findViewById(R.id.todo_time);
//            isAlerted = (TextView) itemView.findViewById(R.id.isAlerted);
            card_background = (ImageView) itemView.findViewById(R.id.card_bg);
            timelineView = (TimelineView) itemView.findViewById(R.id.time_marker);

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

        RequestOptions options2 =new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .signature(new ObjectKey(SPUtils.get(context,"head_signature","")))
                .placeholder(R.drawable.ic_img1);

        ViewHolder.todo_title.setText(todosList.get(todosList.size()-1-i).getTitle());
        ViewHolder.todo_desc.setText(todosList.get(todosList.size()-1-i).getDesc());
        ViewHolder.todo_date.setText(todosList.get(todosList.size()-1-i).getDate() + " "+ todosList.get(todosList.size()-1-i).getTime());
        ViewHolder.card_background.setImageBitmap(BitmapUtils.readBitMap(context, todosList.get(todosList.size()-1-i).getImgId()));


        if (todosList.get(todosList.size()-1-i).getIsRepeat() == 1){
            ViewHolder.isRepeat.setText("重复");
            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }else {
            ViewHolder.isRepeat.setText("单次");
            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }

        if(todosList.get(todosList.size()-1-i).getRemindTime() <= System.currentTimeMillis() ){
            ViewHolder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker));
        }else {
            ViewHolder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.round));
        }


    }

    @Override
    public int getItemCount() {
        return todosList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(todosList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition,toPosition);
        return true;
    }

    public void removeItem(int position){
        truePosition = todosList.size()-1-position;
        itemPosition = position;
        popAlertDialog();
//        todosList.remove(todosList.size()-1-position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position,todosList.size()-position);
    }

    private void popAlertDialog() {

        if (dialog == null) {

            dialog = new MaterialDialog(context);
            dialog.setMessage("确定删除？")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Todos todos = todosList.get(truePosition);
                            dbHelper = new MyDatabaseHelper(context, "Data.db", null, 2);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            db.delete("Todo","todotitle = ?",
                                    new String[]{todosList.get(truePosition).getTitle()});
//                                new ToDoDao(getContext()).deleteTask(todosList);
//                                todosList.delete(new UpdateListener() {
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
                                ToDoUtils.deleteNetTodos(context, todos, new ToDoUtils.DeleteTaskListener() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(int errorCord, String msg) {
                                        Toasty.warning(context, msg, Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                            todosList.remove(truePosition);
                            notifyItemRemoved(itemPosition);
                            notifyItemRangeChanged(itemPosition,todosList.size());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        public void onClick(View view) {
                            notifyItemChanged(itemPosition);
                            dialog.dismiss();
                        }
                    });

        }

        dialog.show();

    }


}