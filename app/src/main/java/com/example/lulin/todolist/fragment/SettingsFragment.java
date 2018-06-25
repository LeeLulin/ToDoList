package com.example.lulin.todolist.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.lulin.todolist.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences);
    }

}

