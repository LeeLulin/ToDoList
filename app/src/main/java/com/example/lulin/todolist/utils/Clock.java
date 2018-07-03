package com.example.lulin.todolist.utils;

import cn.bmob.v3.BmobObject;

public class Clock extends BmobObject{
    private int id;
    private String title;
    private long time;

    public void setId(int id){
        this.id = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setTime(long time){
        this.time = time;
    }

    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public long getTime(){
        return time;
    }
}
