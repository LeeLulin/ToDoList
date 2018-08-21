package com.example.lulin.todolist.Utils;

import android.text.format.DateUtils;

import java.util.concurrent.TimeUnit;

public class TimeFormatUtil {
    public static String formatTime(long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return DateUtils.formatElapsedTime(new StringBuilder(8), seconds);
    }
}
