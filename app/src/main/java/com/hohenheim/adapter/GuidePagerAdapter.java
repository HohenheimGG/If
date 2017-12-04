package com.hohenheim.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hohenheim.view.IconView;
import com.hohenheim.view.TabIndicator;

import java.util.List;

/**
 * Created by hohenheim on 17/12/4.
 */

public class GuidePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public GuidePagerAdapter(FragmentManager manager, List<Fragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
