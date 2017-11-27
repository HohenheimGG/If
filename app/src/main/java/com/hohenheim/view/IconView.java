package com.hohenheim.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hohenheim.R;

public class IconView extends View {

    private Rect mTextRect = new Rect();
    private Paint mTextPaint;
    private String mText;

    private int mColor;
    private float mAlpha;

    private Bitmap mIconBitmap;
    private Rect mIconRect = new Rect();
    private Paint mIconPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    public IconView(Context context, AttributeSet set){
        super(context, set);

        TypedArray value = context.obtainStyledAttributes(set, R.styleable.IconView);

        mText = value.getString(R.styleable.IconView_icon_text);
        int mTextSize = (int)value.getDimension(R.styleable.IconView_icon_text_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mIconBitmap = BitmapFactory.decodeResource(getResources(), set.getAttributeResourceValue("tools", "icon_icon", R.drawable.nav_tool));
        mColor = value.getColor(R.styleable.IconView_icon_color, getResources().getColor(R.color.normal_green));

        value.recycle();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

        mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaint.setColor(mColor);
    }

    public IconView(Context context, AttributeSet set, int d){
        super(context, set, d);
    }

    @Override
    public void onMeasure(int measureWidth, int measureHeight){
        super.onMeasure(measureWidth, measureHeight);
        int width = getMeasuredWidth() * 3 / 4;
        int height = getMeasuredHeight() * 3 / 4;
        int bitmapWidth = Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingBottom() - getPaddingTop() - mTextRect.height());

        int left = (getMeasuredWidth() - bitmapWidth) / 2;
        int top = (getMeasuredHeight() - bitmapWidth - mTextRect.height()) / 2;

        mIconRect.set(left, top, left + bitmapWidth, top + bitmapWidth);
    }

    @Override
    public void onDraw(Canvas canvas){

        int alpha = (int) Math.ceil(255 * mAlpha);
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);

        drawMemoryBitmap(alpha);
        drawSrcText(canvas, alpha);
        drawDstText(canvas, alpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    private void drawMemoryBitmap(int alpha){
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mIconPaint.setAlpha(alpha);
        mIconPaint.setDither(true);
        mCanvas.drawRect(mIconRect, mIconPaint);
        mIconPaint.setAlpha(255);
        mIconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));


        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mIconPaint);
    }

    private void drawSrcText(Canvas canvas, int alpha){
        mTextPaint.setColor(getResources().getColor(R.color.normal_grey));
        mTextPaint.setAlpha(255 - alpha);
        canvas.drawText(mText,
                mIconRect.left + (mIconRect.width() - mTextRect.width()) / 2,
                mIconRect.bottom + mTextRect.height() + 5,
                mTextPaint);
    }

    private void drawDstText(Canvas canvas, int alpha){
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        canvas.drawText(mText,
                mIconRect.left + (mIconRect.width()- mTextRect.width()) / 2,
                mIconRect.bottom + mTextRect.height() + 5,
                mTextPaint);
    }

    public void setIconAlpha(float alpha){
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView(){
        if(Looper.getMainLooper() == Looper.myLooper())
            invalidate();
        else
            postInvalidate();
    }
}
