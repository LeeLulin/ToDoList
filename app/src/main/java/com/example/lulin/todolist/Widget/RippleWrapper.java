package com.example.lulin.todolist.Widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.example.lulin.todolist.R;

import java.util.ArrayList;

public class RippleWrapper extends RelativeLayout {
    private boolean mIsRunning;
    private AnimatorSet mAnimatorSet;
    private int mRippleColor;
    private int mRippleDuration;
    private int mRippleDelay;
    private int mRippleAmount;

    public RippleWrapper(Context context) {
        this(context, null, 0);
    }

    public RippleWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleWrapper);
        mRippleColor = typedArray.getColor(R.styleable.RippleWrapper_rippleColor,
                ContextCompat.getColor(context, R.color.colorRipple));
        mRippleDuration = typedArray.getInteger(R.styleable.RippleWrapper_rippleDuration, 5000);
        mRippleDelay = typedArray.getInteger(R.styleable.RippleWrapper_rippleDelay, 2500);
        mRippleAmount = typedArray.getInteger(R.styleable.RippleWrapper_rippleAmount, 1);
        typedArray.recycle();

        setFocusable(false);
    }

    public void build() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT, TRUE);

        mAnimatorSet = new AnimatorSet();

        ArrayList<Animator> animators = new ArrayList<>();
        for (int i = 0; i < mRippleAmount; i++) {
            RippleView rippleView = new RippleView(getContext(), mRippleColor);
            addView(rippleView, params);

            ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(rippleView, SCALE_X, 1.0f, 1.318f);
            widthAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            widthAnimator.setRepeatMode(ObjectAnimator.RESTART);
            widthAnimator.setDuration(mRippleDuration);
            widthAnimator.setStartDelay(i * mRippleDelay);
            widthAnimator.setInterpolator(new AccelerateInterpolator());
            animators.add(widthAnimator);

            ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(rippleView, SCALE_Y, 1.0f, 1.318f);
            heightAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            heightAnimator.setRepeatMode(ObjectAnimator.RESTART);
            heightAnimator.setDuration(mRippleDuration);
            heightAnimator.setStartDelay(i * mRippleDelay);
            heightAnimator.setInterpolator(new AccelerateInterpolator());
            animators.add(heightAnimator);

            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, View.ALPHA, 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setDuration(mRippleDuration);
            alphaAnimator.setStartDelay(i * mRippleDelay);
            alphaAnimator.setInterpolator(new AccelerateInterpolator());
            animators.add(alphaAnimator);
        }

        mAnimatorSet.playTogether(animators);
        mAnimatorSet.start();
    }


    public void start() {
        if (!mIsRunning) {
            build();
            mIsRunning = true;
        }
    }

    public void stop() {
        if (mIsRunning) {
            mAnimatorSet.end();
            mIsRunning = false;
        }
    }

    private class RippleView extends View {
        private Paint mPaint;
        private int mColor;

        public RippleView(Context context, int color) {
            super(context);
            mColor = color;
            mPaint = getDrawPaint();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();

            int min = width > height ? height : width;
            float half = (float) (min / 2);
            float halfWidth = (float) (width / 2);
            float scale = 0.618f;
            float rippleRadius = half * scale + 4.0f;

            canvas.drawCircle(halfWidth, half, rippleRadius, mPaint);
        }

        private Paint getDrawPaint() {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(mColor);
            paint.setStrokeWidth(3.0f);
            paint.setStyle(Paint.Style.STROKE);
            return paint;
        }
    }
}
