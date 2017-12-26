package com.hohenheim.scancode.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hohenheim.R;
import com.hohenheim.common.activities.BaseActivity;

/**
 * Created by hohenheim on 2017/12/26.
 */

public abstract class ModuleBaseActivity extends BaseActivity implements View.OnClickListener{

    protected abstract int getTitleRes();

    @Override
    protected void initToolBar(Toolbar toolBar) {
        super.initToolBar(toolBar);
        //返回
        findViewById(R.id.iv_title_back_button).setVisibility(View.VISIBLE);
        TextView leftContent = findViewById(R.id.tv_title_back_text);
        leftContent.setVisibility(View.VISIBLE);
        leftContent.setText(getString(R.string.common_back));
        findViewById(R.id.btn_title_left).setOnClickListener(this);
        //标题栏
        ((TextView)findViewById(R.id.tv_title_text)).setText(getString(getTitleRes()));
    }
}
