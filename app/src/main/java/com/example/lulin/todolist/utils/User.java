package com.example.lulin.todolist.utils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

    private BmobFile img;
    private String nickName;

    public User(){}


    public String getNickName(){
        return nickName;
    }
    public BmobFile getImg() {
        return img;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }
}
