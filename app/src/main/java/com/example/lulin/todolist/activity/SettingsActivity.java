package com.example.lulin.todolist.activity;

import android.os.Bundle;

import com.example.lulin.todolist.fragment.SettingsFragment;

public class SettingsActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
