package com.example.lulin.todolist.utils;

import java.io.Serializable;

public class Todos implements Serializable {
    private String title;
    private String desc;
    private String date;
    private String time;
    private long remindTime,remindTimeNoDay;
    private int id,isAlerted,isRepeat,imgId;


    /**
     * Constructs a new instance of {@code Object}.
     */
    public Todos(int id,String title, String desc, String date, String time,long remindTime,long remindTimeNoDay,int isAlerted,int isRepeat,int imgId) {

        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
        this.isAlerted = isAlerted;
        this.remindTime = remindTime;
        this.remindTimeNoDay = remindTimeNoDay;
        this.isRepeat = isRepeat;
        this.imgId = imgId;
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

    public void setRemindTimeNoDay(long remindTimeNoDay){
        this.remindTimeNoDay = remindTimeNoDay;
    }

    public void setIsRepeat(int isRepeat){
        this.isRepeat = isRepeat;
    }

    public void setImgId(int imgId){
        this.imgId = imgId;
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

    public long getRemindTimeNoDay(){
        return remindTimeNoDay;
    }

    public int getIsRepeat(){
        return isRepeat;
    }

    public int getImgId(){
        return imgId;
    }
}