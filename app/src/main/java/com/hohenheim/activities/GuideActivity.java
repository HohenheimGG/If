package com.hohenheim.activities;

import android.graphics.drawable.Icon;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hohenheim.R;
import com.hohenheim.adapter.GuidePagerAdapter;
import com.hohenheim.fragments.BaseFragment;
import com.hohenheim.fragments.RecommendFragment;
import com.hohenheim.fragments.ToolsFragment;
import com.hohenheim.view.IFToolBar;
import com.hohenheim.view.IconView;
import com.hohenheim.view.TabIndicator;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements View.OnClickListener{

    private FragmentManager mFrManager;
    private Fragment reFragment;
    private Fragment toolFragment;

//    private IconView ivRecommend;
//    private IconView ivTool;
//    private TabIndicator tiRecommend;
//    private TabIndicator tiTool;

    private ViewPager viewPager;
    private List<IconView> iconViews = new ArrayList<>();
    private List<TabIndicator> tabIndicators = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_guide);
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
        viewPager.addOnPageChangeListener(new PageChangeListener(iconViews, tabIndicators));
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initToolBar(Toolbar toolBar) {
        super.initToolBar(toolBar);
        IFToolBar bar = (IFToolBar)toolBar;
        TextView tvLeft = (TextView)bar.findViewById(R.id.tv_title_back_text);
        ImageView ivLeft = (ImageView)bar.findViewById(R.id.iv_title_back_button);
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

    private static class PageChangeListener implements ViewPager.OnPageChangeListener {

        private List<IconView> iconViews;
        private List<TabIndicator> tabIndicators;

        private PageChangeListener(List<IconView> iconViews, List<TabIndicator> tabIndicators) {
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
}
