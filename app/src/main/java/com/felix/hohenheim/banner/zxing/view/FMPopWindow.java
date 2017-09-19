package com.felix.hohenheim.banner.zxing.view;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.felix.hohenheim.banner.R;


/**
 * Created by hohenheim on 17/9/5.
 */

class FMPopWindow implements View.OnClickListener{

    private Context context;
    private PopWindowClickListener listener;
    private String message = "";
    private String topContent = "";
    private String bottomContent = "";
    private int buttonNum = 1;
    private int unit_a;

    public FMPopWindow(Context context) {
        this.context = context;
        Point point = new Point();
        ((WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        unit_a = point.x / 60;
    }

    /**
     * popwindow展示的文案
     * @param text
     */
    public FMPopWindow setMessage(String text) {
        this.message = text;
        return this;
    }

    /**
     * 按钮数量 1 <= x <= 2
     * @param num
     */
    public FMPopWindow setButtomNum(int num) {
        this.buttonNum = num;
        return this;
    }

    public FMPopWindow setTopContent(String content) {
        this.topContent = content;
        return this;
    }

    public FMPopWindow setBottonContent(String content) {
        this.bottomContent = content;
        return this;
    }

    public void setClickListener(PopWindowClickListener listener) {
        this.listener = listener;
    }

    public PopupWindow build() {
        int height;
        View view = LayoutInflater.from(this.context).inflate(R.layout.view_capture_pop_window, null);
//        ((TextView)view.findViewById(R.id.message)).setText(this.message);
        Button top = (Button)view.findViewById(R.id.top);
        top.setText(topContent);
        top.setOnClickListener(this);
        Button bottom = (Button)view.findViewById(R.id.bottom);
        bottom.setText(bottomContent);
        bottom.setOnClickListener(this);
        if(this.buttonNum == 1) {
            height = unit_a * 28;
        } else {
            height = unit_a * 41;
            bottom.setVisibility(View.VISIBLE);
        }

        PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, height);
        window.setFocusable(true);
        return window;
    }

    public static void show(PopupWindow window, View parent) {
        window.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.top && this.listener != null) {
            this.listener.onTopPress();
        } else if(id == R.id.bottom && this.listener != null) {
            this.listener.onBottomPress();
        }
    }

    interface PopWindowClickListener {
        void onTopPress();
        void onBottomPress();
    }
}
