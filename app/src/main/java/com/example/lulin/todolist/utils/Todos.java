package com.example.lulin.todolist.utils;

import java.io.Serializable;

public class Todos implements Serializable {
    //标题，内容
    private String title;
    private String desc;


    /**
     * Constructs a new instance of {@code Object}.
     */
    public Todos(String title, String dsc) {

        this.title = title;
        this.desc = dsc;

    }

//    public void setId(Integer id){
//        this.id = id;
//    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public void setTitle(String title) {
        this.title = title;
    }

//    public Integer getId() {
//        return id;
//    }

    public String getDesc() {
        return desc;
    }


    public String getTitle() {
        return title;
    }
}