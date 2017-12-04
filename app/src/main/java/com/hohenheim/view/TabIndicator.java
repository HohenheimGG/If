package com.hohenheim.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hohenheim.R;

/**
 * Created by hohenheim on 2017/12/3.
 */

public class TabIndicator extends View {

    /**
     * 当前View在ViewPager所处位置
     */
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    private int mDirection = DIRECTION_LEFT;
    private float mProgress = 0;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    private int mFocusColor;
    private int mUnFocusColor;

    public TabIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray value = context.obtainStyledAttributes(attrs, R.styleable.TabIndicator);
        mFocusColor = value.getColor(R.styleable.TabIndicator_focus_color,
                getResources().getColor(R.color.normal_green));
        mUnFocusColor = value.getColor(R.styleable.TabIndicator_unfocus_color,
                getResources().getColor(R.color.normal_grey));
        value.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mFocusColor);
        setBackgroundColor(mUnFocusColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        if(mProgress >= 0.5) {
            if(mProgress != 1) {
                canvas.scale(1.5f, 1);
                canvas.translate(-5, 0);
            }
            drawSizeScale(canvas, mProgress - 0.5f);
            return;
        }

        if(mDirection == DIRECTION_LEFT) {
            drawLeftLength(canvas);
        } else if(mDirection == DIRECTION_RIGHT) {
            drawRightLength(canvas);
        }

        drawSizeScale(canvas, 0.5f - mProgress);
    }

    /**
     * 绘制线条粗细变化
     * @param canvas
     */
    private void drawSizeScale(Canvas canvas, float progress) {
        float fraction = progress / 0.5f;
        int height = Math.max((int)(mHeight * fraction), mHeight / 2);
        int radius = height / 2;//半径

        Path path = new Path();
        path.moveTo(radius, mHeight / 2 - radius);//起点
        path.lineTo(mWidth - radius, mHeight / 2 - radius);//画线
        path.cubicTo(mWidth - radius, mHeight / 2 - radius,
                mWidth, mHeight / 2,
                mWidth - radius, mHeight / 2 + radius);//半圆
        path.lineTo(radius, mHeight / 2 + radius);//画线
        path.cubicTo(radius, mHeight / 2 + radius,
                0, mHeight / 2,
                radius, mHeight / 2 - radius);//画圆

        canvas.drawPath(path, mPaint);
    }

    /**
     * 左侧长度变化
     * @param canvas
     */
    private void drawLeftLength(Canvas canvas) {
        float fraction = mProgress / 0.5f;
        int width = (int)(mWidth * fraction);
        Log.d("TAG", "left mProgress: " + mProgress + " width: " + width + " mWidth: " + mWidth);
        canvas.translate(mWidth - width, 0);

    }

    /**
     * 右侧长度变化
     * @param canvas
     */
    private void drawRightLength(Canvas canvas) {
        float fraction = mProgress / 0.5f;
        int width = (int)(mWidth * fraction);
        Log.d("TAG", "right mProgress: " + mProgress + " width: " + width + " mWidth: " + mWidth);
        canvas.translate(width - mWidth, 0);
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        if(Looper.getMainLooper() == Looper.myLooper())
            invalidate();
        else
            postInvalidate();
    }
}
