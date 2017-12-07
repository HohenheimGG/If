package com.hohenheim.common.view.LoopBanner;

import android.support.v4.view.PagerAdapter;

public abstract class BaseBannerAdapter extends PagerAdapter {

    private static final int MAX_NUM = 300000;//总个数

    /**
     * 此处采取的轮播方案是让Adapter中的数量取无限大(Integer.MAX_VALUE),所以需要让view的位置在中间
     * @return 返回view初始化时位置
     */
    int getInitialPosition() {
        return MAX_NUM / 2 - MAX_NUM / 2 % getBannerCount();
    }

    /**
     * 获需要轮播的图片数量
     * @return
     */
    protected abstract int getBannerCount();

    @Override
    public int getCount() {
        int count = getBannerCount();
        if(count == 1)
            return 1;
        return MAX_NUM;
    }
}
