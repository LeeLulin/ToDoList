package com.example.lulin.todolist.utils;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by df on 18-6-23.
 */

public class Time extends BmobObject{
    private Date time;

    private User user;

    public Time(){}

    public void setTime(Date time){this.time=time;}
    public void setUser(User user1){this.user=user1;}
}
