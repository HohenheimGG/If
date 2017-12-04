package com.hohenheim.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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

    private static final int DEFAULT_ORIENTATION = 2;
    private static final int LEFT_TO_RIGHT = 3;
    private static final int RIGHT_TO_LEFT = 4;

    private int mDirection = DIRECTION_LEFT;
    private int mOrientation = LEFT_TO_RIGHT;
    private float mLastProgress = 0;
    private float progress = 0;
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
        setBackgroundColor(mUnFocusColor);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //数据清零
        if(progress == 0) {
            mOrientation = DEFAULT_ORIENTATION;
            mLastProgress = 0;
            return;
        }

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //判断滚动方向
        if(mOrientation == DEFAULT_ORIENTATION && mLastProgress == 0) {
            mLastProgress = progress;
        } else if(mOrientation == DEFAULT_ORIENTATION) {
            if(mLastProgress > progress) {
                mOrientation = RIGHT_TO_LEFT;
            } else {
                mOrientation = LEFT_TO_RIGHT;
            }
        }


        if(mOrientation == LEFT_TO_RIGHT) {
            if(mDirection == DIRECTION_LEFT) {
                if(progress >= 0.5) {
                    drawSizeScale(canvas);
                } else {

                }
            } else if(mDirection == DIRECTION_RIGHT) {
                if(progress >= 0.5) {

                } else {
                    drawSizeScale(canvas);

                }
            }
        }

        //线条粗细变化, 粗 -> 细 || 细 -> 粗
        if(progress >= 0.5) {
            drawSizeScale(canvas);
            return;
        }

        if(mDirection == DIRECTION_LEFT) {
            if(mOrientation == LEFT_TO_RIGHT)
                drawLeftLength(canvas);
            else if(mOrientation == RIGHT_TO_LEFT)
                drawRightLength(canvas);
        } else if (mDirection == DIRECTION_RIGHT) {
            if(mOrientation == RIGHT_TO_LEFT)
                drawRightLength(canvas);
            else if(mOrientation == LEFT_TO_RIGHT)
                drawLeftLength(canvas);
        }

        //重置
        if(progress == 1) {
            mOrientation = DEFAULT_ORIENTATION;
            mLastProgress = 0;
        }
    }

    /**
     * 绘制线条粗细变化
     * @param canvas
     */
    private void drawSizeScale(Canvas canvas) {
        float fraction = (progress - 0.5f) / 0.5f;
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

        mPaint.setColor(mFocusColor);
        canvas.drawPath(path, mPaint);
    }

    /**
     * 左侧长度变化
     * @param canvas
     */
    private void drawLeftLength(Canvas canvas) {
        float fraction = (0.5f - progress) / 0.5f;
        int width = (int)(mWidth * fraction);
        canvas.translate(mWidth - width, 0);

//        int radius = mHeight / 2;//半径
//        float fraction = (0.5f - progress) / 0.5f;
//
//        int width = (int)(mWidth * fraction);
//        Path path = new Path();
//
//        path.moveTo(radius + mWidth - width, mHeight / 2 - radius);
//        path.lineTo(mWidth, mHeight / 2 - radius);
//        path.lineTo(mWidth, mHeight / 2 + radius);
//        path.lineTo(radius + mWidth - width, mHeight / 2 + radius);
//        path.cubicTo(radius + mWidth - width, mHeight / 2 + radius,
//                mWidth - width, mHeight / 2,
//                radius + mWidth - width, mHeight / 2 - radius);
//
//        mPaint.setColor(mFocusColor);
//        canvas.drawPath(path, mPaint);
    }

    private void drawRightScale(Canvas canvas) {
        float fraction =
    }

    /**
     * 右侧长度变化
     * @param canvas
     */
    private void drawRightLength(Canvas canvas) {
        float fraction = (0.5f - progress) / 0.5f;
        int width = (int)(mWidth * fraction);
        canvas.translate(width - mWidth, 0);

//        int radius = mHeight / 2;//半径
//        float fraction = (0.5f - progress) / 0.5f;
//        int width = (int)(mWidth * fraction);
//        Path path = new Path();
//
//        path.moveTo(0, mHeight / 2 - radius);
//        path.lineTo(width - radius, mHeight / 2 - radius);
//        path.cubicTo(width - radius, mHeight / 2 - radius,
//                width, mHeight / 2,
//                width - radius, mHeight / 2 + radius);
//        path.lineTo(0, mHeight / 2 + radius);
//        path.lineTo(0, mHeight / 2 - radius);
//
//        mPaint.setColor(mFocusColor);
//        canvas.drawPath(path, mPaint);
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if(Looper.getMainLooper() == Looper.myLooper())
            invalidate();
        else
            postInvalidate();
    }
}
