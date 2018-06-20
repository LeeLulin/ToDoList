package com.example.lulin.todolist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.FocusService;

import java.util.List;
import java.util.Locale;

public class ClockFragment extends Fragment {

    private Switch focus;
    private UsageStatsManager usageStatsManager;
    private List<UsageStats> queryUsageStats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clock, container, false);

        focus = rootView.findViewById(R.id.set_focus);
        focus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    if (Build.VERSION.SDK_INT > 20){
                        if (!isNoSwitch()){
                            RequestPromission();
                        }
                    }
                    Intent intent = new Intent(getActivity(), FocusService.class);
                    getActivity().startService(intent);
                } else {
                    Intent intent = new Intent(getActivity(), FocusService.class);
                    getActivity().stopService(intent);
                }
            }
        });
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

}
