package com.example.lulin.todolist.activity;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lulin.todolist.DBHelper.MyDatabaseHelper;
import com.example.lulin.todolist.R;
import com.example.lulin.todolist.adapter.FragmentAdapter;
import com.example.lulin.todolist.fragment.ClockFragment;
import com.example.lulin.todolist.fragment.TodoFragment;
import com.example.lulin.todolist.utils.NetWorkUtils;
import com.example.lulin.todolist.utils.SPUtils;
import com.example.lulin.todolist.utils.User;
import com.example.lulin.todolist.widget.CircleImageView;
import com.example.lulin.todolist.widget.ColorFilterToolBar;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.LineMoveIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.drakeet.materialdialog.MaterialDialog;
import top.wefor.circularanim.CircularAnim;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class MainActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DachshundTabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton fab;
    private MyDatabaseHelper dbHelper;
    private CircleImageView user_image;
    private TextView nick_name,autograph;
    public User local_user;
    private ImageView nav_bg;
    private static final String APP_ID = "1c54d5b204e98654778c77547afc7a66";
    private String imgPath;
    private MenuItem mMenuItemIDLE;
    private DrawerLayout drawer;
    private SwitchCompat isFocus;
    private static final String KEY_FOCUS = "focus";
    private UsageStatsManager usageStatsManager;
    private List<UsageStats> queryUsageStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CircularAnim.init(400, 400, R.color.colorPrimary);
        super.onCreate(savedInstanceState);
        //设置状态栏
        setStatusBar();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ColorFilterToolBar toolbar = (ColorFilterToolBar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

                if (mMenuItemIDLE != null && newState == DrawerLayout.STATE_IDLE) {
                    runNavigationItemSelected(mMenuItemIDLE);
                    mMenuItemIDLE = null;
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        nav_bg = headerView.findViewById(R.id.nav_bg);
        nick_name = headerView.findViewById(R.id.nick_name);
        autograph = headerView.findViewById(R.id.user_autograph);
        user_image = headerView.findViewById(R.id.user_image);
        user_image.setOnClickListener(this);

        dbHelper = new MyDatabaseHelper(this, "Data.db", null, 2);
        dbHelper.getWritableDatabase();


        if (NetWorkUtils.isNetworkConnected(getApplication())){

            if (User.getCurrentUser() != null){
                try{
                    setUserDataFromBmob();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }else {
            glideLoad();
        }



        initView();
        initViewPager();
//        initGuide();
    }

    /**
     * 用户引导
     */
    private void initGuide(){
        NewbieGuide.with(this)
                .setLabel("guide1")
//                .setShowCounts(1)//控制次数
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(fab)
                        .setLayoutRes(R.layout.guide_simple))
                .show();
    }

    private void initView() {

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    private void initViewPager() {
        mTabLayout = (DachshundTabLayout) findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.view_pager_main);


        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_title_main_1));
        titles.add(getString(R.string.tab_title_main_2));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TodoFragment());
        fragments.add(new ClockFragment());

        mViewPager.setOffscreenPageLimit(2);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mTabLayout.setAnimatedIndicator(new LineMoveIndicator(mTabLayout));
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapter);

        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if (position == 0) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircularAnim.fullActivity(MainActivity.this, v)
                                .go(new CircularAnim.OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd() {
                                        Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
                                        startActivityForResult(intent,1);
                                    }
                                });
                    }
                });
            } else {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircularAnim.fullActivity(MainActivity.this, v)
                                .go(new CircularAnim.OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd() {
                                        Intent intent = new Intent(MainActivity.this, NewClockActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fab:

                //跳转动画
                CircularAnim.fullActivity(MainActivity.this, view)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                Intent intent = new Intent(MainActivity.this, NewTodoActivity.class);
                                startActivityForResult(intent,1);
                            }
                        });

                break;

            case R.id.user_image:

                CircularAnim.fullActivity(MainActivity.this, view)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                drawer.closeDrawer(GravityCompat.START);
                                if (NetWorkUtils.isNetworkConnected(getApplication())){
                                    local_user = User.getCurrentUser(User.class);
                                }
                                if (local_user != null){
                                    Intent intent = new Intent(MainActivity.this, UserDataActivity.class);
                                    startActivityForResult(intent, 1);
                                } else {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivityForResult(intent,1);
                                }
                            }
                        });


                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent,1);

            return true;
        }
        if (id == R.id.menu_focus){
            final MaterialDialog focusDialog = new MaterialDialog(MainActivity.this);
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.dialog_focus, null);
            focusDialog.setTitle("专注模式");
            focusDialog.setView(view);
            isFocus = view.findViewById(R.id.sw_focus);
            isFocus.setChecked(getIsFocus(this));
            isFocus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if (Build.VERSION.SDK_INT > 20){
                            if (!isNoSwitch()){
                                RequestPromission();
                            }
                        }
                        SPUtils.put(MainActivity.this, KEY_FOCUS, isChecked);
                    } else {
                        SPUtils.put(MainActivity.this, KEY_FOCUS, isChecked);
                    }
                }
            });
            focusDialog.setCanceledOnTouchOutside(true);
            focusDialog.show();// 显示对话框
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            mMenuItemIDLE = item;
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }

    /**
     * DrawerLayout 关闭卡顿的综合解决方法
     *
     * @link https://stackoverflow.com/questions/18343018/optimizing-drawer-and-activity-launching-speed
     */
    private void runNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_todo:
                mViewPager.setCurrentItem(0);

                break;

            case R.id.nav_clock:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.nav_frends:

                Intent intent3 = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent3);

                break;

            case R.id.nav_about:

                Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent1);

                break;

            case R.id.nav_setting:

                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent2,1);

                break;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //回调刷新
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2){
            finish();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (resultCode ==3){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
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

    /**
     * 从Bmob加载用户信息
     */
    private void setUserDataFromBmob(){
        User user = User.getCurrentUser(User.class);
        BmobQuery<User> bmobQuery = new BmobQuery();
        bmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                nick_name.setText(user.getNickName());
                autograph.setText(user.getAutograph());
                BmobFile bmobFile = user.getImg();
                bmobFile.download(new DownloadFileListener() {
                    @Override
                    public void done(String path, BmobException e) {
                        if(e==null){
                            Log.i("MainActivity", "保存路径: " + path);
                            imgPath = path;
                            SPUtils.put(MainActivity.this, "path", imgPath);
                            glideLoad();
                        }else{
                            Log.i("MainActivity", "下载失败");
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });

            }
        });
    }

    /**
     * Glide图片加载
     */
    private void glideLoad(){

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(SPUtils.get(MainActivity.this,"head_signature","")));

        Glide.with(getApplicationContext())
                .load(SPUtils.get(MainActivity.this, "path" ,""))
                .apply(options)
                .into(user_image);

        Glide.with(getApplicationContext())
                .load(SPUtils.get(MainActivity.this, "path" ,""))
                .apply(bitmapTransform(new BlurTransformation(25, 3)))
                .apply(options)
                .into(nav_bg);
    }

    public boolean getIsFocus(Context context){

        Boolean isFocus = (Boolean) SPUtils.get(context, KEY_FOCUS, false);

        return isFocus;

    }

    /**
     * 判断“查看应用使用情况”是否开启
     * @return
     */
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        if(Build.VERSION.SDK_INT >=21){
            //noinspection ResourceType
            usageStatsManager = (UsageStatsManager)this.getApplicationContext().getSystemService("usagestats");
            queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts);
        }
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 跳转到“查看应用使用情况”页面
     */
    public void RequestPromission() {
        new AlertDialog.Builder(this).
                setTitle("设置").
                //setMessage("开启usagestats权限")
                        setMessage(String.format(Locale.US,"打开专注模式请允App查看应用的使用情况。"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        //finish();
                    }
                }).show();
    }

}
