package com.hohenheim.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.view.IFToolBar;


public class GuideActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initToolBar(Toolbar toolBar) {
        super.initToolBar(toolBar);
        IFToolBar bar = (IFToolBar)toolBar;
        TextView tvLeft = (TextView)bar.findViewById(R.id.tv_title_back_text);
        ImageView ivLeft = (ImageView)bar.findViewById(R.id.iv_title_back_button);
        ivLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.drawable.if_logo);
        tvLeft.setText(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide, menu);
        return true;
    }
}
