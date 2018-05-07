package com.example.lulin.todolist.utils;

import java.io.Serializable;

public class Todos implements Serializable {
    //标题，内容
    private String title;
    private String desc;
    private String date;
    private String time;


    /**
     * Constructs a new instance of {@code Object}.
     */
    public Todos(String title, String dsc, String date, String time) {

        this.title = title;
        this.desc = dsc;
        this.date = date;
        this.time = time;
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
}