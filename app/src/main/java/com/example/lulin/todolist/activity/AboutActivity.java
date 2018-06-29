package com.example.lulin.todolist.activity;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lulin.todolist.BuildConfig;
import com.example.lulin.todolist.R;

import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;

public class AboutActivity extends AbsAboutActivity {

    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText("生命不息，奋斗不止");
        version.setText("v" + BuildConfig.VERSION_NAME);
    }


    @Override
    protected void onItemsCreated(@NonNull Items items) {
        items.add(new Category("介绍"));
        items.add(new Card(getString(R.string.about_app)));

        items.add(new Category("功能特性"));
        items.add(new Card(getString(R.string.about_function)));

        items.add(new Category("开发者"));
        items.add(new Contributor(R.drawable.designer1,"LeeLulin", "Developer & designer", "https://github.com/LeeLulin"));
        items.add(new Contributor(R.drawable.designer2,"Snowaaaas", "Developer & designer","https://github.com/Snowaaaas"));
        items.add(new Contributor(R.drawable.designer3,"xiaoying8023", "Developer & designer","https://github.com/xiaoying8023"));
        items.add(new Contributor(R.drawable.designer4,"VinceDing123", "Developer & designer","https://github.com/VinceDing123"));

        items.add(new Category("项目地址"));
        items.add(new Contributor(R.drawable.github,"Github","ToDoList","https://github.com/LeeLulin/ToDoList"));

    }
}
