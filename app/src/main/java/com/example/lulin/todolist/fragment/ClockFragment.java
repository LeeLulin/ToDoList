package com.example.lulin.todolist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.FocusService;
import com.example.lulin.todolist.SpacesItemDecoration;
import com.example.lulin.todolist.activity.ClockActivity;
import com.example.lulin.todolist.adapter.ClockRecyclerViewAdapter;
import com.example.lulin.todolist.adapter.TodoRecyclerViewAdapter;
import com.example.lulin.todolist.utils.Clock;
import com.example.lulin.todolist.utils.RecyclerItemClickListener;
import com.example.lulin.todolist.utils.SPUtils;
import com.example.lulin.todolist.utils.ToDoUtils;
import com.example.lulin.todolist.utils.ToastUtils;
import com.example.lulin.todolist.utils.Todos;
import com.example.lulin.todolist.utils.User;
import com.example.lulin.todolist.widget.ClockView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClockFragment extends Fragment {

    private Switch focus;
    private UsageStatsManager usageStatsManager;
    private List<UsageStats> queryUsageStats;
    private Button start_clock;
    private ClockView clockView;
    private int clicked = 0;
    private MyDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ClockRecyclerViewAdapter clockRecyclerViewAdapter;
    private List<Clock> clockList = new ArrayList<>();
    private SQLiteDatabase db;
    private LinearLayoutManager layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "Data.db", null, 2);
        db = dbHelper.getWritableDatabase();
        setList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clock, container, false);

        layout = new LinearLayoutManager(getContext());
//       layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//       layout.setReverseLayout(true);//列表翻转
        recyclerView = (RecyclerView) rootView.findViewById(R.id.clock_recycler_view);
        setUI();
//        boolean isFocus = (Boolean) SPUtils.get(getContext(),"isFocus",true);
//        start_clock = rootView.findViewById(R.id.btn_clock);
//        clockView = rootView.findViewById(R.id.clockView);
//        focus = rootView.findViewById(R.id.set_focus);
//        if (isFocus){
//            focus.setChecked(true);
//        } else {
//            focus.setChecked(false);
//        }
//        focus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked){
//                    if (Build.VERSION.SDK_INT > 20){
//                        if (!isNoSwitch()){
//                            RequestPromission();
//                        }
//                    }
//                    Intent intent = new Intent(getActivity(), FocusService.class);
//                    getActivity().startService(intent);
//                    SPUtils.put(getContext(),"isFocus",true);
//                } else {
//                    Intent intent = new Intent(getActivity(), FocusService.class);
//                    getActivity().stopService(intent);
//                    SPUtils.put(getContext(),"isFocus",false);
//                }
//            }
//        });
//
//        start_clock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ClockActivity.class);
//                startActivity(intent);
//            }
//        });
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

    @Override
    public void onResume(){
        setList();
        setUI();
        super.onResume();

    }

    private void setUI(){
        clockRecyclerViewAdapter = new ClockRecyclerViewAdapter(clockList, getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.setAdapter(clockRecyclerViewAdapter);
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
                String clockTitle = clockList.get(clockRecyclerViewAdapter.getItemCount()-1-position).getTitle();
                Intent intent = new Intent(getActivity(), ClockActivity.class);
                intent.putExtra("clocktitle",clockTitle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                Snackbar.make(view, "是否删除？（滑动取消）", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String clockTitle = clockList.get(clockRecyclerViewAdapter.getItemCount()-1-position).getTitle();
//                                Todos todos = todosList.get(todoRecyclerViewAdapter.getItemCount() - 1 - position);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("Todo","todotitle = ?",
                                        new String[]{clockList.get(clockRecyclerViewAdapter.getItemCount() - 1 - position).getTitle()});
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

                                clockRecyclerViewAdapter.removeItem(position);

                            }
                        }).show();

            }
        }));
    }


    /**
     * 判断“查看应用使用情况”是否开启
     * @return
     */
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        if(Build.VERSION.SDK_INT >=21){
            usageStatsManager = (UsageStatsManager) getActivity().getApplicationContext().getSystemService("usagestats");
            queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        }
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 跳转到“查看应用使用情况”页面
     */
    public void RequestPromission() {
        new AlertDialog.Builder(getContext()).
                setTitle("设置").
                //setMessage("开启usagestats权限")
                        setMessage(String.format(Locale.US,"打开专注模式请允App查看应用的使用情况。"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        //finish();
                    }
                }).show();
    }

    private List<Clock> setList(){
        List<Clock> dbList = new ArrayList<Clock>();
        Cursor cursor=db.rawQuery("SELECT * FROM Clock", null);
        while(cursor.moveToNext()) {
            Clock clock = new Clock();
            clock.setTitle(cursor.getString(cursor.getColumnIndex("clocktitle")));
            dbList.add(clock);
        }
        cursor.close();
        clockList = dbList;
        return clockList;

    }

}
