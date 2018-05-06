package com.example.lulin.todolist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.SpacesItemDecoration;
import com.example.lulin.todolist.activity.EditTodoActivity;
import com.example.lulin.todolist.adapter.TodoRecyclerViewAdapter;
import com.example.lulin.todolist.utils.RecyclerItemClickListener;
import com.example.lulin.todolist.utils.Todos;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Todos> todosList;
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;
    private MyDatabaseHelper dbHelper;
    private String todoTitle,todoDsc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMenu();
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
        recyclerView.addItemDecoration(new SpacesItemDecoration(15));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Snackbar.make(view, "短按", Snackbar.LENGTH_LONG)
//                        .setAction("确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//
//                            }
//                        }).show();
                String title = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getTitle();
                String dsc = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getDesc();
                Intent intent = new Intent(getActivity(), EditTodoActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("dsc", dsc);
                startActivity(intent);
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


        // Inflate the layout for this fragment
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
                todoTitle = cursor.getString(cursor.getColumnIndex("todotitle"));
                todoDsc = cursor.getString(cursor.getColumnIndex("tododsc"));
                Todos data = new Todos(todoTitle,todoDsc);
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

}
