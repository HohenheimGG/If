package com.hohenheim.common.view.popupmenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
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
        initView(context, menus, view);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        if(mDismissListener != null)
            popupWindow.setOnDismissListener(mDismissListener);
        popupWindow.showAsDropDown(anchor);
    }

    private void initView(Context context, List<String> menus, View parent) {
        RecyclerView rvView = (RecyclerView)parent.findViewById(R.id.menu_container);
        rvView.setLayoutManager(new GridLayoutManager(context, 1));
        rvView.addItemDecoration(new MenuItemDecoration(context));
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