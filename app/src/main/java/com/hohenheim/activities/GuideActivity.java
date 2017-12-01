package com.hohenheim.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


public class GuideActivity extends BaseActivity implements View.OnClickListener{

    private FragmentManager mFrManager;
    private Fragment reFragment;
    private Fragment toolFragment;

    private IconView ivRecommend;
    private IconView ivTool;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    public void findViews() {
        ivRecommend = (IconView)findViewById(R.id.iv_label_recommend);
        ivTool = (IconView)findViewById(R.id.iv_label_tool);

        ivRecommend.setOnClickListener(this);
        ivTool.setOnClickListener(this);
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
        FragmentTransaction transaction;
        switch(v.getId()) {
            case R.id.iv_label_recommend:
                transaction = mFrManager.beginTransaction();
                transaction.replace(R.id.fr_layout_module, reFragment);
                transaction.commit();
                ivRecommend.setFraction(1);
                ivTool.setFraction(0);
                break;
            case R.id.iv_label_tool:
                transaction = mFrManager.beginTransaction();
                transaction.replace(R.id.fr_layout_module, toolFragment);
                transaction.commit();
                ivRecommend.setFraction(0);
                ivTool.setFraction(1);
                break;
        }
    }
}
