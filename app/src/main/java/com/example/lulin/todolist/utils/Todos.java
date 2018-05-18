package com.example.lulin.todolist.utils;

import java.io.Serializable;

public class Todos implements Serializable {
    //标题，内容
    private String title;
    private String desc;
    private String date;
    private String time;
    private long remindTime;
    private int id,isAlerted;


    /**
     * Constructs a new instance of {@code Object}.
     */
    public Todos(int id,String title, String desc, String date, String time,long remindTime,int isAlerted) {

        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
        this.isAlerted = isAlerted;
        this.remindTime = remindTime;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setisAlerted(int hasAlerted){
        this.isAlerted = hasAlerted;
    }

    public void setRemindTime(long remindTime){
        this.remindTime = remindTime;
    }



    public int getId(){
        return id;
    }

    public String getDesc() {
        return desc;
    }


    public String getTitle() {
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public int getisAlerted(){
        return isAlerted;
    }

    public long getRemindTime(){
        return remindTime;
    }
}