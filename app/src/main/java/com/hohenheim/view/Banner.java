package com.hohenheim.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.felix.hohenheim.hohenheim.R;
import com.hohenheim.adapter.BasePagerAdapter;
import com.hohenheim.transformer.BasePageTransformer;

import java.lang.ref.WeakReference;

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener{

    private static final int DURATION = 3000;//轮播时间
    private static final int SCROLLING = 0;//滑动状态
    private static final int IDLE = 1;//静止状态

    private int curState = IDLE;//当前状态
    private boolean isAutoPlay = true;//是否自动轮播
    private boolean isWheel = true; //是否轮播图
    private ViewPager viewPager;
    private BasePagerAdapter adapter;

    private Handler handler = new Handler() {
        WeakReference<Banner> ref = new WeakReference<>(Banner.this);
        @Override
        public void handleMessage(Message msg) {
            if(msg.what != SCROLLING)
                return;
            Banner banner;
            if((banner = ref.get())!= null) {
                banner.viewPager.setCurrentItem(banner.viewPager.getCurrentItem() + 1);
                startAutoPlay();
            }
        }
    };

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        viewPager = (ViewPager)LayoutInflater.from(context).inflate(R.layout.view_banner_layout, this, false);
        addView(viewPager);
        viewPager.setPageMargin(40);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 是否自动轮播
     */
    public void setAutoPlay(boolean bool) {
        this.isAutoPlay = bool;
        //当该组件可以自动轮播时, 默认为轮播图
        isWheel = bool || isWheel;
    }

    /**
     * 是否轮播图
     */
    public void setWheel(boolean bool) {
        this.isWheel = bool;
    }

    /**
     * 开始轮播
     */
    public void startAutoPlay() {
        if(isAutoPlay) {
            handler.removeMessages(SCROLLING);
            handler.sendEmptyMessageDelayed(SCROLLING, DURATION);
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        handler.removeMessages(SCROLLING);
    }

    public void setAdapter(BasePagerAdapter adapter) {
        if(adapter == null)
            return;
        this.adapter = adapter;
        this.viewPager.setAdapter(adapter);
        if(!isWheel)
            return;
        setInitialPosition();
    }

    /**
     * 当banner为轮播图时, 图片处于中间
     */
    private void setInitialPosition() {
        this.viewPager.setCurrentItem(this.adapter.getInitialPosition());
    }

    /**
     * 轮播动画
     * @param former
     */
    public void setPageTransformer(BasePageTransformer former) {
        this.setPageTransformer(true, former);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, BasePageTransformer former) {
        viewPager.setPageTransformer(reverseDrawingOrder, former);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch(state) {
            case ViewPager.SCROLL_STATE_IDLE://view处于静止状态
                if(curState != state)
                    startAutoPlay();
                curState = IDLE;
                break;
            case ViewPager.SCROLL_STATE_DRAGGING://view处于手动拖动状态
            case ViewPager.SCROLL_STATE_SETTLING://view处于自动滚动状态
                if(curState == ViewPager.SCROLL_STATE_IDLE)
                    stopAutoPlay();
                curState = SCROLLING;
                break;
        }
    }
}
