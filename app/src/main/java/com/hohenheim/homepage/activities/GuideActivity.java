package com.hohenheim.homepage.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hohenheim.R;
import com.hohenheim.homepage.adapter.GuidePagerAdapter;
import com.hohenheim.common.activities.BaseActivity;
import com.hohenheim.homepage.fragments.RecommendFragment;
import com.hohenheim.homepage.fragments.ToolsFragment;
import com.hohenheim.common.view.IFToolBar;
import com.hohenheim.common.view.IconView;
import com.hohenheim.common.view.TabIndicator;
import com.hohenheim.homepage.listener.GuidePageChangeListener;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements View.OnClickListener{

    private FragmentManager mFrManager;
    private Fragment reFragment;
    private Fragment toolFragment;

    private ViewPager viewPager;
    private List<IconView> iconViews = new ArrayList<>();
    private List<TabIndicator> tabIndicators = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void setContentView() {
        setContentView(R.layout.homepage_activity_guide);
    }

    @Override
    public void findViews() {
        iconViews.add((IconView)findViewById(R.id.iv_label_recommend));
        iconViews.add((IconView)findViewById(R.id.iv_label_tool));

        tabIndicators.add((TabIndicator)findViewById(R.id.ti_recommend));
        tabIndicators.add((TabIndicator)findViewById(R.id.ti_tool));

        viewPager = (ViewPager)findViewById(R.id.view_page);
    }

    @Override
    public void getData() {
        mFrManager = getSupportFragmentManager();
        reFragment = RecommendFragment.newInstance();
        toolFragment = ToolsFragment.newInstance();

        fragments.add(reFragment);
        fragments.add(toolFragment);
    }

    @Override
    public void showContent() {
        viewPager.setAdapter(new GuidePagerAdapter(mFrManager, fragments));
        viewPager.addOnPageChangeListener(new GuidePageChangeListener(iconViews, tabIndicators));
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initToolBar(Toolbar toolBar) {
        super.initToolBar(toolBar);
        TextView tvLeft = (TextView)findViewById(R.id.tv_title_back_text);
        ImageView ivLeft = (ImageView)findViewById(R.id.iv_title_back_button);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.drawable.if_logo_white);
        tvLeft.setText(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
