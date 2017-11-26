package com.hohenheim.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import com.hohenheim.R;
import com.hohenheim.view.IFToolBar;

/**
 * Created by hohenheim on 2017/11/25.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        super.setContentView(R.layout.activity_base);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().
                    getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

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

    protected void initToolBar(Toolbar toolBar) {
        ((IFToolBar)toolBar).setStartColor(Color.parseColor("#FF9800"));
        ((IFToolBar)toolBar).setEndColor(Color.parseColor("#F57C00"));
        setSupportActionBar(toolBar);
    }

}
