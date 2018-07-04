package com.example.lulin.todolist.utils;

import cn.bmob.v3.BmobObject;

public class Tomato extends BmobObject {
    private String title;
    private User user;
    private Clock clock;

    public void setTitle(String title){
        this.title = title;
    }
    public void setClock(Clock clock){
        this.clock = clock;
    }
    public void setUser(User user){
        this.user = user;
    }

    public String getTitle(){
        return title;
    }
    public Clock  getClock(){
        return clock;
    }
    public User getUser(){
        return user;
    }
}
