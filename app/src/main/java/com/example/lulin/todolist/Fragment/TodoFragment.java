package com.example.lulin.todolist.Fragment;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.SpacesItemDecoration;
import com.example.lulin.todolist.Adapter.TodoRecyclerViewAdapter;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.RecyclerItemClickListener;
import com.example.lulin.todolist.Utils.SPUtils;
import com.example.lulin.todolist.Utils.SimpleItemTouchHelperCallback;
import com.example.lulin.todolist.Utils.ToDoUtils;
import com.example.lulin.todolist.Utils.ToastUtils;
import com.example.lulin.todolist.Utils.Todos;
import com.example.lulin.todolist.Utils.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Todos> todosList = new ArrayList<>();
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;
    private MyDatabaseHelper dbHelper;
    private String todoTitle,todoDsc,todoDate,todoTime;
    private int id,isAlerted,isRepeat,imgId;
    private long remindTime,remindTimeNoDay;
    private User currentUser;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "Data.db", null, 2);
        if(NetWorkUtils.isNetworkConnected(getContext())) {
            if (User.getCurrentUser() != null){
                currentUser = BmobUser.getCurrentUser(User.class);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
//       layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//       layout.setReverseLayout(true);//列表翻转
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todosList,getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(todoRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
////                String title = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getTitle();
////                String dsc = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getDesc();
////                String date = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getDate();
////                String time = todosList.get(todoRecyclerViewAdapter.getItemCount()-1-position).getTime();
////                Intent intent = new Intent(getActivity(), EditTodoActivity.class);
////                intent.putExtra("title", title);
////                intent.putExtra("dsc", dsc);
////                intent.putExtra("date", date);
////                intent.putExtra("time", time);
////                startActivityForResult(intent,1);
//            }
//
//            @Override
//            public void onItemLongClick(View view, final int position) {
//                Snackbar.make(view, "是否删除？（滑动取消）", Snackbar.LENGTH_LONG)
//                        .setAction("确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Todos todos = todosList.get(todoRecyclerViewAdapter.getItemCount() - 1 - position);
//                                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                                db.delete("Todo","todotitle = ?",
//                                        new String[]{todosList.get(todoRecyclerViewAdapter.getItemCount() - 1 - position).getTitle()});
////                                new ToDoDao(getContext()).deleteTask(todos);
////                                todos.delete(new UpdateListener() {
////                                    @Override
////                                    public void done(BmobException e) {
////                                        if (e==null){
////                                            todoRecyclerViewAdapter.removeItem(position);
////                                        } else {
////                                            ToastUtils.showShort(getContext(),e.getMessage());
////                                        }
////                                    }
////                                });
//
//                                if (User.getCurrentUser(User.class) != null){
//                                    ToDoUtils.deleteNetTodos(getContext(), todos, new ToDoUtils.DeleteTaskListener() {
//                                        @Override
//                                        public void onSuccess() {
//
//                                        }
//
//                                        @Override
//                                        public void onError(int errorCord, String msg) {
//                                            ToastUtils.showShort(getContext(),msg);
//                                        }
//                                    });
//                                }
//
//                                todoRecyclerViewAdapter.removeItem(position);
//
//                            }
//                        }).show();
//
//            }
//        }));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initRefreshLayout();
        if (NetWorkUtils.isNetworkConnected(getContext())){
            if (User.getCurrentUser() != null){
                query();
            }
        }
        getMyTask();


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
     * 设置RecyclerView
     */
    private void initRefreshLayout() {


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

    /**
     * 获取我的任务list
     */
    private void getMyTask() {

        boolean isSync = (Boolean) SPUtils.get(getActivity(),"sync",true);
        // 1.首先获取本地数据库
        List<Todos> todos = ToDoUtils.getAllTodos(getContext());
        if (todos.size() > 0) {
            setListData(todos);
        }


        if (NetWorkUtils.isNetworkConnected(getContext())){
            if (currentUser != null){
                // 获取网络，可能是换手机了，或者是没有添加过，或者是当前时间以后没有
                if (todos.size() <= 0) {
                    ToDoUtils.getNetAllTodos(getContext(), currentUser, new ToDoUtils.GetTodosCallBack() {
                        @Override
                        public void onSuccess(List<Todos> todos) {
                            if (todos != null){
                                setListData(todos);
                            }
                        }

                        @Override
                        public void onError(int errorCode, String msg) {

                        }
                    });
                }
            }
        }

    }

    /**
     * 设置list数据
     */
    private void setListData(List<Todos> newList) {
        todosList.addAll(newList);
        todoRecyclerViewAdapter.notifyDataSetChanged();
    }


}
