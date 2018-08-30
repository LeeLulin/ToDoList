package com.example.lulin.todolist.Bean;


import android.graphics.drawable.Drawable;

public class App {

    private int id;
    private String packageName;
    private Drawable image;
    private String appName;
    private byte[] imageBlob;



    public int getId(){
        return id;
    }

    public Drawable getImage() {
        return image;
    }

    public String getPackageName() {
        return packageName;
    }



    public String getName() {
        return appName;
    }

    public byte[] getImageBlob(){
        return imageBlob;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String appName) {
        this.appName = appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setImageBlob(byte[] imageBlob){
        this.imageBlob = imageBlob;
    }


}
