package com.hohenheim.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hohenheim.R;
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

    private IconView ivRecommend;
    private IconView ivTool;

    private List<TabIndicator> list = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    public void findViews() {
//        ivRecommend = (IconView)findViewById(R.id.iv_label_recommend);
//        ivTool = (IconView)findViewById(R.id.iv_label_tool);
//
//        ivRecommend.setOnClickListener(this);
//        ivTool.setOnClickListener(this);

        fragments.add(RecommendFragment.newInstance());
        fragments.add(ToolsFragment.newInstance());
        fragments.add(RecommendFragment.newInstance());
        list.add((TabIndicator) findViewById(R.id.tab_1));
        list.add((TabIndicator)findViewById(R.id.tab_2));
        list.add((TabIndicator)findViewById(R.id.tab_3));

        ViewPager viewpager = (ViewPager)findViewById(R.id.view_page);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position != list.size() - 1) {
                    TabIndicator leftView = list.get(position);
                    TabIndicator rightView = list.get(position + 1);
                    leftView.setDirection(TabIndicator.DIRECTION_LEFT);
                    rightView.setDirection(TabIndicator.DIRECTION_RIGHT);
                    if(positionOffset > 0.99)
                        positionOffset = 1f;
                    leftView.setProgress(1 - positionOffset);
                    rightView.setProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);
    }

    @Override
    public void getData() {
        mFrManager = getSupportFragmentManager();
        reFragment = RecommendFragment.newInstance();
        toolFragment = ToolsFragment.newInstance();
    }

    @Override
    public void showContent() {

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
//        FragmentTransaction transaction;
//        switch(v.getId()) {
//            case R.id.iv_label_recommend:
//                transaction = mFrManager.beginTransaction();
//                transaction.replace(R.id.fr_layout_module, reFragment);
//                transaction.commit();
//                ivRecommend.setFraction(1);
//                ivTool.setFraction(0);
//                break;
//            case R.id.iv_label_tool:
//                transaction = mFrManager.beginTransaction();
//                transaction.replace(R.id.fr_layout_module, toolFragment);
//                transaction.commit();
//                ivRecommend.setFraction(0);
//                ivTool.setFraction(1);
//                break;
//        }
    }
}
