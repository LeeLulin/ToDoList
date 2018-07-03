package com.example.lulin.todolist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference implements SeekBar.OnSeekBarChangeListener {
    private static final int DEFAULT_MAX_VALUE = 100;
    private static final int DEFAULT_MIN_VALUE = 1;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SeekBar mSeekBar;
    private TextView mSeekBarValue;
    private String mKey;
    private int mUnitID;

    private int mMax = DEFAULT_MAX_VALUE;
    private int mMin = DEFAULT_MIN_VALUE;
    private int mProgress;

    public SeekBarPreference(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
    }

    public void build() {
        if (mSeekBar != null) {
            mSeekBar.setMax(mMax - mMin);
            mSeekBar.setOnSeekBarChangeListener(this);
            mSeekBar.setEnabled(true);

            setText(mProgress);
        }
    }

    public SeekBarPreference setSeekBar(SeekBar seekBar) {
        mSeekBar = seekBar;
        mKey = mContext.getResources().getResourceEntryName(seekBar.getId());

        return this;
    }

    public SeekBarPreference setSeekBarValue(TextView textView) {
        mSeekBarValue = textView;
        return this;
    }

    public SeekBarPreference setMax(int max) {
        if (max != mMax) {
            mMax = max;
        }

        return this;
    }

    public SeekBarPreference setMin(int min) {
        if (min != mMin) {
            mMin = min;
        }

        return this;
    }

    public SeekBarPreference setUnit(int resId) {
        mUnitID = resId;
        return this;
    }

    public SeekBarPreference setProgress(int progress) {
        updateProgress(progress);
        return this;
    }

    private void updateProgress(int progress) {
        if (progress > mMax) {
            progress = mMax;
        } else if (progress < mMin) {
            progress = mMin;
        }

        mProgress = progress;

        if (mSeekBar != null) {
            mSeekBar.setProgress(progress - mMin);
        }

        persistInt(progress);
    }

    private void setText(int progress) {
        if (mSeekBarValue != null) {
            mSeekBarValue.setText(mContext.getResources()
                    .getString(mUnitID, progress));
        }
    }

    private void persistInt(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mKey, value);

        try {
            editor.apply();
        } catch (AbstractMethodError unused) {
            // The app injected its own pre-Gingerbread
            // SharedPreferences.Editor implementation without
            // an apply method.
            editor.commit();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress = progress + mMin;
        if (fromUser) {
            updateProgress(progress);
        }

        setText(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
