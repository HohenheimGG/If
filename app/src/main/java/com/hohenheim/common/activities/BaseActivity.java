package com.hohenheim.common.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import com.hohenheim.R;
import com.hohenheim.common.view.IFToolBar;

/**
 * Created by hohenheim on 2017/11/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        super.setContentView(R.layout.common_activity_base);
        this.setTransparentStatusBar();
        init();
    }

    private void init(){
        setContentView();
        findViews();
        getData();
        showContent();
    }


    public abstract void setContentView();
    public abstract void findViews();
    public abstract void getData();
    public abstract void showContent();

    /**
     * 设置状态栏透明
     */
    private void setTransparentStatusBar() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 将Child Activity的layout添加进Parent Activity的layout中
     * @param layoutId
     */
    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        LinearLayout rootLayout = (LinearLayout)findViewById(R.id.root_layout);
        if(rootLayout == null)
            return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        initToolBar((Toolbar)findViewById(R.id.tool_bar_layout));
    }

    /**
     * 初始化toolbar
     * @param toolBar
     */
    protected void initToolBar(Toolbar toolBar) {
        ((IFToolBar)toolBar).setStartColor(getResources().getColor(R.color.theme_main));
        ((IFToolBar)toolBar).setEndColor(getResources().getColor(R.color.theme_main));
        setSupportActionBar(toolBar);
    }
}
