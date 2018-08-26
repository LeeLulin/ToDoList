package com.example.lulin.todolist.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Bean.User;
import com.example.lulin.todolist.Utils.NetWorkUtils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends BasicActivity implements View.OnClickListener {
    private EditText mEtUserName;
    private EditText mEtPassWord;
    private Button mBtnRegister;
    private ImageView mBtnGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_register);
        mEtUserName = (EditText) findViewById(R.id.register_name);
        mEtPassWord = (EditText) findViewById(R.id.register_pwd);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnGoLogin = (ImageView) findViewById(R.id.back_login);
        mBtnRegister.setOnClickListener(this);
        mBtnGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:

                final String username = mEtUserName.getText().toString();
                final String password = mEtPassWord.getText().toString();
                //判断网络情况
                if(NetWorkUtils.isNetworkConnected(getApplication())){

                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        Toasty.info(RegisterActivity.this, "请输入用户名或密码", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    if (mEtUserName.length() < 4) {
                        Toasty.info(RegisterActivity.this, "用户名不能低于4位", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    if (mEtPassWord.length() < 6) {
                        Toasty.info(RegisterActivity.this, "密码不能低于6位", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    /**
                     * Bmob注册
                     */
                    final User user = new User();
                    final String path = this.getApplicationContext().getFilesDir().getAbsolutePath() + "/default_head.png";
                    Log.i("register", path);
                    final BmobFile bmobFile = new BmobFile(new File(path));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){

                                Log.i("register", "上传成功！" + bmobFile.getUrl());
                                user.setUsername(username);
                                user.setPassword(password);
                                user.setNickName(username);
                                user.setAutograph("个性签名");
                                user.setImg(bmobFile);
                                user.setTotal(0);
                                user.signUp(new SaveListener<User>() {
                                    @Override
                                    public void done(User s, BmobException e) {
                                        if(e==null){
                                            Toasty.success(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT, true).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Toasty.error(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT, true).show();
                                            Log.i("register", e.getMessage());
                                        }
                                    }
                                });

                            }else {
                                Log.i("register", "失败！ " + e.getMessage() + path);
                            }

                        }

                        @Override
                        public void onProgress(Integer value) {
                            // 返回的上传进度（百分比）
                        }
                    });

                } else {
                    Toasty.error(RegisterActivity.this, "无网络连接", Toast.LENGTH_SHORT, true).show();
                }



                break;

            case R.id.back_login:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }



    }


    /**
     * 设置状态栏透明
     */
    private void setStatusBar(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
