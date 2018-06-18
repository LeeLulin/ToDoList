package com.example.lulin.todolist.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Service.FocusService;

public class ClockFragment extends Fragment {

    private Switch focus;
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
}
