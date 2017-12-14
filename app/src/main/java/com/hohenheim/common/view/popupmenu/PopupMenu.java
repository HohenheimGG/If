package com.hohenheim.common.view.popupmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hohenheim.R;

import java.util.List;

/**
 * Created by hohenheim on 2017/12/14.
 */

/**
 * Created by hohenheim on 17/12/14.
 */

public class PopupMenu {

    private PopupWindow.OnDismissListener mDismissListener;

    private PopupWindow popupWindow;

    public PopupMenu() {

    }

    public void show(View anchor, List<String> menus) {
        Context context = anchor.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.common_view_menu_container, null);
        initView(context, menus);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        if(mDismissListener != null)
            popupWindow.setOnDismissListener(mDismissListener);
        popupWindow.showAsDropDown(anchor);
    }

    private void initView(Context context, List<String> menus) {
        RecyclerView rvView = new RecyclerView(context);
        rvView.setLayoutManager(new GridLayoutManager(context, 1));
        rvView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvView.setAdapter(new PopupMenuAdapter(menus));
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener dismissListener) {
        this.mDismissListener = dismissListener;
    }

    public void dismiss() {
        if(popupWindow != null)
            popupWindow.dismiss();
        popupWindow = null;
    }
}