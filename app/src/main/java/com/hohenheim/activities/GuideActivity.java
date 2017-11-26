package com.hohenheim.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText("IF");
    }
}
