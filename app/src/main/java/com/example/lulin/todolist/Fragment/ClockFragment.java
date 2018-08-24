package com.example.lulin.todolist.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.lulin.todolist.Activity.ClockActivity;
import com.example.lulin.todolist.Adapter.ClockRecyclerViewAdapter;
import com.example.lulin.todolist.Utils.ClockItemTouchHelperCallback;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.RecyclerItemClickListener;
import com.example.lulin.todolist.Utils.ToastUtils;
import com.example.lulin.todolist.Bean.Tomato;
import com.example.lulin.todolist.Utils.TomatoUtils;
import com.example.lulin.todolist.Bean.User;

import java.util.ArrayList;
import java.util.List;

public class ClockFragment extends Fragment {

    private MyDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ClockRecyclerViewAdapter clockRecyclerViewAdapter;
    private List<Tomato> clockList = new ArrayList<>();
    private SQLiteDatabase db;
    private LinearLayoutManager layout;
    private User currentUser;
    private List<Tomato> tomato;
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper.Callback callback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "Data.db", null, 2);
        db = dbHelper.getWritableDatabase();
        if(NetWorkUtils.isNetworkConnected(getContext())) {
            currentUser = User.getCurrentUser(User.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clock, container, false);
        layout = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.clock_recycler_view);
        clockRecyclerViewAdapter = new ClockRecyclerViewAdapter(clockList, getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.setAdapter(clockRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String clockTitle = clockList.get(clockRecyclerViewAdapter.getItemCount()-1-position).getTitle();
                Intent intent = new Intent(getActivity(), ClockActivity.class);
                intent.putExtra("clocktitle",clockTitle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
        }));

        callback = new ClockItemTouchHelperCallback(clockRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        setDbData();
        setNetData();

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

    @Override
    public void onResume(){
        setDbData();
//        setUI();
        clockRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();

    }



    private void setDbData(){

        tomato = TomatoUtils.getAllTomato(getContext());
        if (tomato.size() > 0) {
            setListData(tomato);
        }



    }

    private void setNetData(){
        if (NetWorkUtils.isNetworkConnected(getContext())){
            if (currentUser != null){
                // 获取网络，可能是换手机了，或者是没有添加过，或者是当前时间以后没有
                if (tomato.size() <= 0) {
                    TomatoUtils.getNetAllTomato(getContext(), currentUser, new TomatoUtils.GetTomatoCallBack() {
                        @Override
                        public void onSuccess(List<Tomato> tomato) {
                            if (tomato != null){
                                setListData(tomato);
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
    private void setListData(List<Tomato> newList) {
        clockList.clear();
        clockList.addAll(newList);
        clockRecyclerViewAdapter.notifyDataSetChanged();
    }

}
