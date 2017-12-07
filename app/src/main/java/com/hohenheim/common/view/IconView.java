package com.hohenheim.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.hohenheim.R;
import com.hohenheim.common.utils.ColorUtils;
import com.hohenheim.common.utils.ImageResize;

public class IconView extends View {

    private int mFocusColor;
    private int mUnFocusColor;
    private int bitmapRes;

    private float fraction;
    private Bitmap mIconBitmap;
    private Bitmap mBitmap;
    private Rect mIconRect;
    private RectF mIconRectF;
    private Paint mIconPaint;
    private Xfermode xfermode;
    private Canvas mCanvas;

    public IconView(Context context, AttributeSet set){
        super(context, set);

        TypedArray value = context.obtainStyledAttributes(set, R.styleable.IconView);
        bitmapRes = value.getResourceId(R.styleable.IconView_icon_icon, R.drawable.nav_tool);
        mFocusColor = value.getColor(R.styleable.IconView_icon_focus_color, getResources().getColor(R.color.normal_green));
        mUnFocusColor = value.getColor(R.styleable.IconView_icon_unfocus_color, getResources().getColor(R.color.normal_grey));
        value.recycle();

        mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaint.setAntiAlias(true);
        mIconRect = new Rect();
        mIconRectF = new RectF();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    public IconView(Context context, AttributeSet set, int d){
        super(context, set, d);
    }

    @Override
    public void onMeasure(int measureWidth, int measureHeight){
        super.onMeasure(measureWidth, measureHeight);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int bitmapWidth = Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingBottom() - getPaddingTop());

        int left = (getMeasuredWidth() - bitmapWidth) / 2;
        int top = (getMeasuredHeight() - bitmapWidth) / 2;

        mIconRect.set(left, top, left + bitmapWidth, top + bitmapWidth);
        mIconRectF.set(mIconRect);
        mIconBitmap = ImageResize.decodeBitmapFromRes(getResources(), bitmapRes, bitmapWidth, bitmapWidth);
    }

    @Override
    public void onDraw(Canvas canvas){
        int color = ColorUtils.evaluateColor(Math.pow(fraction, 2), mUnFocusColor, mFocusColor);
        mIconPaint.setColor(color);

        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        drawMemoryBitmap();
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private void drawMemoryBitmap(){
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mIconPaint.setDither(true);
        mCanvas.drawRect(mIconRect, mIconPaint);
        mIconPaint.setXfermode(xfermode);

        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mIconPaint);
        mIconPaint.setXfermode(null);
    }

    public void setFraction(float fraction){
        this.fraction = fraction;
        invalidateView();
    }

    private void invalidateView(){
        if(Looper.getMainLooper() == Looper.myLooper())
            invalidate();
        else
            postInvalidate();
    }
}
