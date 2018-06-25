package com.example.lulin.todolist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.SpacesItemDecoration;
import com.example.lulin.todolist.activity.EditTodoActivity;
import com.example.lulin.todolist.activity.LoginActivity;
import com.example.lulin.todolist.activity.MainActivity;
import com.example.lulin.todolist.activity.NewTodoActivity;
import com.example.lulin.todolist.adapter.TodoRecyclerViewAdapter;
import com.example.lulin.todolist.utils.NetWorkUtils;
import com.example.lulin.todolist.utils.RecyclerItemClickListener;
import com.example.lulin.todolist.utils.Title;
import com.example.lulin.todolist.utils.Todos;
import com.example.lulin.todolist.utils.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.wefor.circularanim.CircularAnim;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Todos> todosList;
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;
    private MyDatabaseHelper dbHelper;
    private String todoTitle,todoDsc,todoDate,todoTime;
    private int id,isAlerted,isRepeat,imgId;
    private long remindTime,remindTimeNoDay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
        if (NetWorkUtils.isNetworkConnected(getApplicationContext())){

            if (User.getCurrentUser() != null){
                query();
            }
        }
        query();
    }

    private void initPersonData() {
        todosList =new ArrayList<>();
        dbData();
    }
    public void initMenu(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
//       layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//       layout.setReverseLayout(true);//列表翻转
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        initPersonData();
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todosList,getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                String title = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getTitle();
//                String dsc = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getDesc();
//                String date = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getDate();
//                String time = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getTime();
//                Intent intent = new Intent(getActivity(), EditTodoActivity.class);
//                intent.putExtra("title", title);
//                intent.putExtra("dsc", dsc);
//                intent.putExtra("date", date);
//                intent.putExtra("time", time);
//                startActivityForResult(intent,1);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                Snackbar.make(view, "是否删除？（滑动取消）", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("Todo","todotitle = ?",new String[]{todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getTitle()});
                                todoRecyclerViewAdapter.removeItem(position);

                            }
                        }).show();

            }
        }));

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * SQLite与list数据绑定
     */
    public void dbData(){
        dbHelper = new MyDatabaseHelper(getContext(), "Data.db", null, 2);
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        try{
            Cursor cursor=db.rawQuery("SELECT * FROM Todo", null);
            while(cursor.moveToNext()) {

//                id = cursor.getInt(cursor.getColumnIndex("id"));
//                todoTitle = cursor.getString(cursor.getColumnIndex("todotitle"));
//                todoDsc = cursor.getString(cursor.getColumnIndex("tododsc"));
//                todoDate = cursor.getString(cursor.getColumnIndex("tododate"));
//                todoTime = cursor.getString(cursor.getColumnIndex("todotime"));
//                remindTime = cursor.getLong(cursor.getColumnIndex("remindTime"));
//                remindTimeNoDay = cursor.getLong(cursor.getColumnIndex("remindTimeNoDay"));
//                isAlerted = cursor.getInt(cursor.getColumnIndex("isAlerted"));
//                isRepeat = cursor.getInt(cursor.getColumnIndex("isRepeat"));
//                imgId = cursor.getInt(cursor.getColumnIndex("imgId"));
                Todos data = new Todos();
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setTitle(cursor.getString(cursor.getColumnIndex("todotitle")));
                data.setDesc(cursor.getString(cursor.getColumnIndex("tododsc")));
                data.setDate(cursor.getString(cursor.getColumnIndex("tododate")));
                data.setTime(cursor.getString(cursor.getColumnIndex("todotime")));
                data.setRemindTime(cursor.getLong(cursor.getColumnIndex("remindTime")));
                data.setRemindTimeNoDay(cursor.getLong(cursor.getColumnIndex("remindTimeNoDay")));
                data.setisAlerted(cursor.getInt(cursor.getColumnIndex("isAlerted")));
                data.setIsRepeat(cursor.getInt(cursor.getColumnIndex("isRepeat")));
                data.setImgId(cursor.getInt(cursor.getColumnIndex("imgId")));
                todosList.add(data);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }finally{

            if(db.isOpen()){
                db.close();
            }
        }
    }

    private void query(){
        User user = User.getCurrentUser(User.class);
        BmobQuery<Todos>  bmobQuery = new BmobQuery<Todos>();
        bmobQuery.addWhereEqualTo("user", user);
        bmobQuery.findObjects(new FindListener<Todos>() {
            @Override
            public void done(List<Todos> object, BmobException e) {
                if (e==null){
                    for (Todos todos : object){
                        Log.i("main", "查询到："+String.valueOf(object.size())+"组数据:"+todos.getTitle()+" "+todos.getDesc()+" "+todos.getBmobDate().getDate());
                    }
                } else {
                    Log.i("main", e.getMessage());
                }
            }
        });
    }

}
