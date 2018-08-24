package com.example.lulin.todolist.Bean;


import cn.bmob.v3.BmobObject;

public class Clock extends BmobObject{
    private int id;
    private String title;
    private long time;
    private String start_time;
    private String end_time;
    private long duration;
    private String date_add;
    private User user;

    public void setId(int id){
        this.id = id;
    }
    public void setUser(User user){
        this.user = user;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setTime(long time){
        this.time = time;
    }
    public void setStart_time(String start_time){
        this.start_time = start_time;
    }
    public void setEnd_time(String end_time){
        this.end_time = end_time;
    }
    public void setDuration(long duration){
        this.duration = duration;
    }
    public void setDate_add(String date_add){
        this.date_add = date_add;
    }

    public int getId(){
        return id;
    }
    public User getUser(){
        return user;
    }
    public String getTitle(){
        return title;
    }
    public long getTime(){
        return time;
    }
    public String getStart_time(){
        return start_time;
    }
    public String getEnd_time(){
        return end_time;
    }
    public long getDuration(){
        return duration;
    }
    public String getDate_add(){
        return date_add;
    }


}
