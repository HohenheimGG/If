package com.hohenheim.homepage.listener;

import android.support.v4.view.ViewPager;

import com.hohenheim.common.view.IconView;
import com.hohenheim.common.view.TabIndicator;

import java.util.List;

/**
 * Created by hohenheim on 2017/12/23.
 */

public class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

    private List<IconView> iconViews;
    private List<TabIndicator> tabIndicators;

    public GuidePageChangeListener(List<IconView> iconViews, List<TabIndicator> tabIndicators) {
        this.iconViews = iconViews;
        this.tabIndicators = tabIndicators;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position != iconViews.size() - 1) {
            IconView leftIcon = iconViews.get(position);
            IconView rightIcon = iconViews.get(position + 1);
            leftIcon.setFraction(1 - positionOffset);
            rightIcon.setFraction(positionOffset);

            TabIndicator leftIndicator = tabIndicators.get(position);
            TabIndicator rightIndicator = tabIndicators.get(position + 1);
            leftIndicator.setDirection(TabIndicator.DIRECTION_LEFT);
            rightIndicator.setDirection(TabIndicator.DIRECTION_RIGHT);
            leftIndicator.setProgress(1 - positionOffset);
            rightIndicator.setProgress(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
