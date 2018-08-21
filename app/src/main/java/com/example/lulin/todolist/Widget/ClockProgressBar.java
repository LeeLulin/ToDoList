package com.example.lulin.todolist.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.lulin.todolist.R;

public class ClockProgressBar extends View {
    private int mColorArc;
    private int mColorCircle;
    private long mMaxProgress;
    private long mProgress;
    private Paint mPaintArc;
    private Paint mPaintCircle;
    private RectF mRectF;

    public ClockProgressBar(Context context) {
        this(context, null, 0);
    }

    public ClockProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mColorArc = ContextCompat.getColor(context, R.color.colorStrokeArc);
        mColorCircle = ContextCompat.getColor(context, R.color.colorStrokeCircle);

        setFocusable(false);
        initPainters();
    }

    private void initPainters() {
        mPaintCircle = getPaint();
        mPaintCircle.setColor(mColorCircle);
        mPaintArc = getPaint();
        mPaintArc.setColor(mColorArc);

        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setRectF();
        canvas.drawArc(mRectF, -90.0f, 360.0f, false, mPaintCircle);
        canvas.drawArc(mRectF, -90.0f, getSweepAngle(), false, mPaintArc);
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6.0f);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    private void setRectF() {
        int width = getWidth();
        int height = getHeight();

        int size = width > height ? height : width;
        float radius = (float) (size / 2);
        float halfWidth = (float) (width / 2);

        float scale = 0.618f; // 黄金分割
        float top = radius - radius * scale;
        float left = halfWidth - radius * scale;
        float right = halfWidth + radius * scale;
        float bottom = radius + radius * scale;
        mRectF.set(left, top, right, bottom);
    }

    private float getSweepAngle() {
        return 360.0f * ((float) mProgress / (float) mMaxProgress);
    }

    public void setMaxProgress(long maxProgress) {
        mMaxProgress = maxProgress;
    }

    public void setProgress(long progress) {
        mProgress = mMaxProgress - progress;
        invalidate();
    }
}
