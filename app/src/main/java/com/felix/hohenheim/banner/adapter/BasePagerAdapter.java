package com.felix.hohenheim.banner.adapter;

import android.support.v4.view.PagerAdapter;

public abstract class BasePagerAdapter extends PagerAdapter {

    static final int MAX_NUM = 300000;//总个数

    /**
     * 当banner为轮播图时需重写此方法.
     * 此处采取的轮播方案是让Adapter中的数量取无限大(Integer.MAX_VALUE),所以需要让view的位置在中间
     * @return 返回view初始化时位置
     */
    public int getInitialPosition() {
        return 0;
    }

}
