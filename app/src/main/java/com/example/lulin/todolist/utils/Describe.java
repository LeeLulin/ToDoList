package com.example.lulin.todolist.utils;

import cn.bmob.v3.BmobObject;

/**
 * Created by df on 18-6-24.
 */

public class Describe extends BmobObject {
    private String describe;
    private User user;

    public Describe(){}

    public void setDescribe(String describe){
        this.describe=describe;
    }

    public void setUser(User user3){
        this.user=user3;
    }
}
