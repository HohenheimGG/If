package com.hohenheim.common.view;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.hohenheim.common.adapter.PopWindowAdapter;

/**
 * Created by com.hohenheim on 17/9/19.
 */

public class ScanPopWindow {

    private PopWindowAdapter adapter;
    private int unit_a;
    private PopupWindow scanPopWindow;

    public ScanPopWindow(Context context) {
        Point point = new Point();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        unit_a = point.x / 60;
    }

    public void setAdapter(PopWindowAdapter adapter) {
        this.adapter = adapter;
        scanPopWindow = build();
    }

    private PopupWindow build() {
        int height = unit_a * 44;
        scanPopWindow = new PopupWindow(this.adapter.getView(), ViewGroup.LayoutParams.MATCH_PARENT, height);
        scanPopWindow.setFocusable(true);
        return scanPopWindow;
    }

    public void show(View parent) {
        scanPopWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void dismiss() {
        if(scanPopWindow != null)
            scanPopWindow.dismiss();
    }
}
