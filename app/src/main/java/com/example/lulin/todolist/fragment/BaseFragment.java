package com.example.lulin.todolist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.lulin.todolist.utils.User;

import cn.bmob.v3.BmobUser;

public class BaseFragment extends Fragment {

    protected Context context;
    protected User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        currentUser = User.getCurrentUser(User.class);
    }
}