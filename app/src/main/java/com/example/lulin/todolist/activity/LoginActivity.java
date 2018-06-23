package com.example.lulin.todolist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.NetWorkUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BasicActivity {
    private EditText mEtUserName = null;
    private EditText mEtPassWord = null;
    private SharedPreferences login_sp;
    private CheckBox mRememberCheck;
    private Button mBtnGoLogin,mBtnGoRegister;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_login);
        login_sp= PreferenceManager.getDefaultSharedPreferences(this);
        mEtUserName = (EditText) findViewById(R.id.et_login_name);
        mEtPassWord = (EditText) findViewById(R.id.et_login_pwd);
        mRememberCheck = (CheckBox) findViewById(R.id.login_remember);
        boolean isRemenber = login_sp.getBoolean("remember_password",false);
        mBtnGoLogin = (Button) findViewById(R.id.btn_login);
        mBtnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mEtUserName.getText().toString();
                final String password = mEtPassWord.getText().toString();

                if (NetWorkUtils.isNetworkConnected(getApplicationContext())) {


                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final BmobUser user = new BmobUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(new SaveListener<BmobUser>() {
                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, UserDataActivity.class);
                                startActivityForResult(intent,1);
//                                startActivity(intent);
                                //记住密码
                                editor=login_sp.edit();
                                if(mRememberCheck.isChecked()){
                                    editor.putBoolean("remember_password",true);
                                    editor.putString("account",username);
                                    editor.putString("password",password);
                                }else {
                                    editor.clear();
                                }
                                editor.apply();
//                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "无网络连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //跳转注册页面
        mBtnGoRegister = (Button) findViewById(R.id.btn_register);
        mBtnGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(isRemenber){
            //将账号和密码都设置到文本中
            String account=login_sp.getString("account","");
            String password=login_sp.getString("password","");
            mEtUserName.setText(account);
            mEtPassWord.setText(password);
            mRememberCheck.setChecked(true);

        }

    }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            setResult(2, intent);
            finish();
        }
//        else if (requestCode == 10) {
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (!Settings.canDrawOverlays(this)) {
//                    // SYSTEM_ALERT_WINDOW permission not granted...
//                    Toast.makeText(MainActivity.this,"not granted",Toast.LENGTH_SHORT);
//                }
//            }
//        }
//        else if (requestCode == 3){
//            setUserData();
//        }

    }


}
