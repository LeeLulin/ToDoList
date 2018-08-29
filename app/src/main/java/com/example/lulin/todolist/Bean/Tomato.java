package com.example.lulin.todolist.Bean;

import cn.bmob.v3.BmobObject;

public class Tomato extends BmobObject {
    private String title;
    private User user;
    private Clock clock;
    private int imgId;
    private int workLength;
    private int shortBreak;
    private int longBreak;
    private int frequency;

    public void setTitle(String title){
        this.title = title;
    }
    public void setClock(Clock clock){
        this.clock = clock;
    }
    public void setUser(User user){
        this.user = user;
    }
    public void setImgId(int imgId){
        this.imgId = imgId;
    }
    public void setWorkLength(int workLength){
        this.workLength = workLength;
    }
    public void setShortBreak(int shortBreak){
        this.shortBreak = shortBreak;
    }
    public void setLongBreak(int longBreak){
        this.longBreak = longBreak;
    }
    public void setFrequency(int frequency){
        this.frequency = frequency;
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
    public int getImgId(){
        return imgId;
    }
    public int getWorkLength(){
        return workLength;
    }
    public int getShortBreak(){
        return shortBreak;
    }
    public int getLongBreak(){
        return longBreak;
    }
    public int getFrequency(){
        return frequency;
    }
}
