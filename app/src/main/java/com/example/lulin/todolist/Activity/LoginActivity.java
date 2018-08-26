package com.example.lulin.todolist.Activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lulin.todolist.Interpolator;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.Utils.NetWorkUtils;
import com.example.lulin.todolist.Utils.SPUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;
import me.drakeet.materialdialog.MaterialDialog;

public class LoginActivity extends BasicActivity implements OnClickListener {

    private Button mBtnLogin;
    private View progress;
    private View mInputLayout;
    private float mWidth, mHeight;
    private LinearLayout mName, mPsw;
    private EditText mEtUserName,mEtPassWord;
    private SharedPreferences login_sp;
    private SharedPreferences.Editor editor;
    private CheckBox mRememberCheck;
    private TextView sign_in,skip_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStatusBar();
        setContentView(R.layout.activity_login);
        login_sp= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemenber = login_sp.getBoolean("remember_password",false);
        initView();
        if(isRemenber){
            //将账号和密码都设置到文本中
            String account=login_sp.getString("account","");
            String password=login_sp.getString("password","");
            mEtUserName.setText(account);
            mEtPassWord.setText(password);
            mRememberCheck.setChecked(true);

        }
    }

    private void initView() {
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        progress = findViewById(R.id.login_layout_progress);
        mInputLayout = findViewById(R.id.login_input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        mEtUserName = (EditText) findViewById(R.id.input_login_name);
        mEtPassWord = (EditText)  findViewById(R.id.input_login_pwd);
        mRememberCheck = (CheckBox) findViewById(R.id.login_remember);
        sign_in = (TextView) findViewById(R.id.sign_in);
        skip_login = (TextView) findViewById(R.id.skip_login);
        skip_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );

        sign_in.setOnClickListener(this);
        skip_login.setOnClickListener(this);

        mBtnLogin.setOnClickListener(new animationOnClickListener(this, mBtnLogin));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in:
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
                break;

            case R.id.skip_login:

                final MaterialDialog skipDialog = new MaterialDialog(this);
                skipDialog.setTitle("提示")
                        .setMessage("跳过登录将无法使用云同步功能，数据将无法备份，是否跳过")
                        .setPositiveButton("跳过", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SPUtils.put(getApplication(),"sync",false);
                                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent2);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new OnClickListener() {
                            public void onClick(View view) {
                                skipDialog.dismiss();
                            }
                        });

                skipDialog.show();

                break;
        }



    }

    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new Interpolator());
        animator3.start();

    }

    class animationOnClickListener implements View.OnClickListener{
        private Context context;
        private TextView btnLogin;


        public animationOnClickListener(Context context, Button btnLogin){
            this.btnLogin = btnLogin;
            this.context = context;
        }

        @Override
        public void onClick(View view){

            if(btnLogin.getVisibility() == View.VISIBLE){

                btnLogin.setVisibility(View.GONE);

                // 计算出控件的高与宽
                mWidth = btnLogin.getMeasuredWidth();
                mHeight = btnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);

                inputAnimator(mInputLayout, mWidth, mHeight);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final String username = mEtUserName.getText().toString();
                        final String password = mEtPassWord.getText().toString();

                        if (NetWorkUtils.isNetworkConnected(getApplication())) {

                            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//                                Toast.makeText(LoginActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                                Toasty.info(LoginActivity.this, "请输入账号或密码", Toast.LENGTH_SHORT, true).show();
                                recovery();
                                return;
                            }

                            final BmobUser user = new BmobUser();
                            user.setUsername(username);
                            user.setPassword(password);
                            user.login(new SaveListener<BmobUser>() {
                                @Override
                                public void done(BmobUser bmobUser, BmobException e) {
                                    if (e == null) {
                                        SPUtils.put(getApplication(),"sync",true);
//                                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                        Toasty.success(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT, true).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
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
                                        finish();
                                    } else {
                                        recovery();
//                                        Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                                        Toasty.error(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            });
                        } else {
                            recovery();
//                            Toast.makeText(LoginActivity.this, "无网络连接！", Toast.LENGTH_SHORT).show();
                            Toasty.error(LoginActivity.this, "无网络连接", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, 2000);

            }

        }
    }

    /**
     * 恢复初始状态
     */
    private void recovery() {

        progress.setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.VISIBLE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
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
}
