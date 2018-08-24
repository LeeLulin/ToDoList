package com.example.lulin.todolist.Bean;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

    private BmobFile img;
    private String nickName;
    private String autograph;
    private String sex;
    private Bitmap localImg;
    private Integer total;

    public User(){}

    public String getSex(){return sex;}
    public String getNickName(){
        return nickName;
    }
    public String getAutograph(){return autograph;}
    public BmobFile getImg() {
        return img;
    }
    public Bitmap getLocalImg(){
        return localImg;
    }
    public Integer getTotal(){
        return total;
    }


    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public void setAutograph(String autograph){this.autograph = autograph;}
    public void setImg(BmobFile img) {
        this.img = img;
    }
    public void setSex(String sex){this.sex = sex;}
    public void setLocalImg(Bitmap localImg){
        this.localImg = localImg;
    }
    public void setTotal(Integer total){
        this.total = total;
    }
}
