package com.example.lulin.todolist.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Receiver.NetworkReceiver;
import com.example.lulin.todolist.Service.AlarmService;
import com.example.lulin.todolist.utils.FileUtils;
import com.example.lulin.todolist.utils.NetWorkUtils;

import cn.bmob.v3.Bmob;
import site.gemus.openingstartanimation.LineDrawStrategy;
import site.gemus.openingstartanimation.NormalDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;

public class SplashActivity extends BasicActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private static final String APP_ID = "1c54d5b204e98654778c77547afc7a66";
    private NetworkReceiver networkReceiver;
    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        //复制assets下的资源文件到sd卡
        fileUtils = new FileUtils();
        fileUtils.copyData(getApplicationContext());

        if (NetWorkUtils.isNetworkConnected(getApplication())){
            Bmob.initialize(getApplication(), APP_ID);
        }


        startService(new Intent(this, AlarmService.class));
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setDrawStategy(new NormalDrawStrategy()) //设置动画效果
//                .setAppIcon() //设置图标
//                .setColorOfAppIcon() //设置绘制图标线条的颜色
//                .setAppName() //设置app名称
//                .setColorOfAppName() //设置app名称颜色
                .setAppStatement("你要内心温柔，安静努力") //设置一句话描述
//                .setColorOfAppStatement() // 设置一句话描述的颜色
                .create();
        openingStartAnimation.show(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                finish();
//                unregisterReceiver(networkReceiver);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
