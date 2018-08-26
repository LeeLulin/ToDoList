package com.example.lulin.todolist;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.example.lulin.todolist.Widget.CircleImageView;
import com.example.lulin.todolist.Widget.DisInterceptNestedScrollView;


/**
 * 类用于头像的行为 具体使用在Strings.xml中的引用
 */
public class CircleImageInUsercBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private final String TAG_TOOLBAR = "user_toolbar";

    private float mStartAvatarY;

    private float mStartAvatarX;

    private int mAvatarMaxHeight;

    private int mToolBarHeight;

    private float mStartDependencyY;

    public CircleImageInUsercBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof DisInterceptNestedScrollView;
    }


    //当dependency变化的时候调用
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        //初始化一些基础参数
        init(parent, child, dependency);
        //计算比例
        if (child.getY() <= 0) return false;
        float percent = (child.getY() - mToolBarHeight) / (mStartAvatarY - mToolBarHeight);

        if (percent < 0) {
            percent = 0;
        }
        if (this.percent == percent || percent > 1) return true;
        this.percent = percent;
       //设置头像的大小
        ViewCompat.setScaleX(child, percent);
        ViewCompat.setScaleY(child, percent);

        return false;
    }

    /**
     * 初始化数据
     * @param parent
     * @param child
     * @param dependency
     */
    private void init(CoordinatorLayout parent, CircleImageView child, View dependency) {
        if (mStartAvatarY == 0) {
            mStartAvatarY = child.getY();
        }
        if (mStartDependencyY == 0) {
            mStartDependencyY = dependency.getY();
        }
        if (mStartAvatarX == 0) {
            mStartAvatarX = child.getX();
        }

        if (mAvatarMaxHeight == 0) {
            mAvatarMaxHeight = child.getHeight();
        }
        if (mToolBarHeight == 0) {
            Toolbar toolbar = (Toolbar) parent.findViewById(R.id.user_toolbar);
            mToolBarHeight = toolbar.getHeight();
        }
    }

    float percent = 0;
}
