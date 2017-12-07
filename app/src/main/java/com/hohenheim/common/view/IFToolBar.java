package com.hohenheim.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.hohenheim.R;
import com.hohenheim.common.utils.ColorUtils;

/**
 * Created by hohenheim on 2017/10/3.
 */

public class IFToolBar extends Toolbar {

    public static final int START_COLOR = R.color.gradually_start;
    public static final int END_COLOR = R.color.gradually_end;

    private int startColor;
    private int endColor;
    private Paint mPaint;
    private float windowWidth;
    private int height;

    public IFToolBar(Context context) {
        this(context, null);
    }

    public IFToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IFToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        windowWidth = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IFToolBar);
        startColor = a.getColor(R.styleable.IFToolBar_start_color, getResources().getColor(START_COLOR));
        endColor = a.getColor(R.styleable.IFToolBar_end_color, getResources().getColor(END_COLOR));
        a.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 1; i <= windowWidth; i++) {
            // 设置画笔颜色为自定义颜色
            mPaint.setColor(ColorUtils.evaluateColor(Math.pow(i / windowWidth, 2), startColor, endColor));
            canvas.drawRect(i - 1, 0, i, height, mPaint);
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
    }

    public void setStartColor(int color) {
        this.startColor = color;
    }

    public void setEndColor(int color) {
        this.endColor = color;
    }
}
