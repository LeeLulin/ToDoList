package com.example.lulin.todolist.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lulin.todolist.Dao.ToDoDao;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.SpacesItemDecoration;
import com.example.lulin.todolist.Adapter.TodoRecyclerViewAdapter;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.TodoItemTouchHelperCallback;
import com.example.lulin.todolist.Utils.ToDoUtils;
import com.example.lulin.todolist.Bean.Todos;
import com.example.lulin.todolist.Bean.User;

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
    private User currentUser;
    private ItemTouchHelper mItemTouchHelper;
    private List<Todos> localTodo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(NetWorkUtils.isNetworkConnected(getContext())) {
            try{
                if (User.getCurrentUser() != null){
                    currentUser = BmobUser.getCurrentUser(User.class);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todosList,getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        ItemTouchHelper.Callback callback = new TodoItemTouchHelperCallback(todoRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        setDbData();
//                        setNetData();
//                    }
//                });
//            }
//        }).start();
        setDbData();
        setNetData();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume(){
        setDbData();
        todoRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();

    }




    private void setDbData(){
        localTodo = ToDoUtils.getAllTodos(getContext());
        if (localTodo.size() > 0) {
            setListData(localTodo);
        }
    }
    /**
     * 获取我的任务list
     */
    private void setNetData() {

        if (NetWorkUtils.isNetworkConnected(getContext())){
            if (currentUser != null){
                // 获取网络，可能是换手机了，或者是没有添加过，或者是当前时间以后没有
                ToDoUtils.getNetAllTodos(getContext(), currentUser, new ToDoUtils.GetTodosCallBack() {
                    @Override
                    public void onSuccess(List<Todos> listTodos) {
                        if (localTodo.size() < listTodos.size()){
                            new ToDoDao(getContext()).clearAll();
                            if ( listTodos!= null){
                                setListData(listTodos);
                                new ToDoDao(getContext()).saveAll(listTodos);
                            }
                        }
                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                    }
                });

            }
        }

    }

    /**
     * 设置list数据
     */
    private void setListData(List<Todos> newList) {
        todosList.clear();
        todosList.addAll(newList);
        todoRecyclerViewAdapter.notifyDataSetChanged();
    }


}
