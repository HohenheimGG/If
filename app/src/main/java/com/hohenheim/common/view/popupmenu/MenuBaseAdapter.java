package com.hohenheim.common.view.popupmenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.hohenheim.R;
import com.hohenheim.common.application.IfApplication;

/**
 * Created by hohenheim on 2017/12/16.
 */

public abstract class MenuBaseAdapter {

    private ColorDrawable mBackgroundColor;

    /**
     * 获取PopupMenu的UI
     * @return
     */
    public abstract View getView();

    /**
     * 设置PopupMenu的LayoutParams
     * @return
     */
    public abstract int[] getLayoutParams();

    /**
     * 设置背景颜色
     * @return
     */
    public Drawable getBackgroundDrawable() {
        if(mBackgroundColor == null)
            mBackgroundColor = new ColorDrawable(IfApplication.getContext().getResources().getColor(R.color.white));
        return mBackgroundColor;
    }

    /**
     * 初始化点击, 焦点等参数
     * @param popupWindow
     */
    public void init(PopupWindow popupWindow) {
        if(popupWindow == null)
            return;
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
    }

    public static class DefaultMenuAdapter extends MenuBaseAdapter {

        private Context context;
        private SparseArrayCompat<String> menus;

        public DefaultMenuAdapter(Context context, SparseArrayCompat<String> menus) {
            this.context = context;
            this.menus = menus;
        }

        @Override
        public View getView() {
            View parent = LayoutInflater.from(context).inflate(R.layout.common_view_menu_container, null);
            RecyclerView rvView = (RecyclerView)parent.findViewById(R.id.menu_container);
            rvView.setLayoutManager(new GridLayoutManager(context, 1));
            rvView.addItemDecoration(new MenuItemDecoration(context));
            rvView.setAdapter(new MenuRVAdapter(menus));
            return parent;
        }

        @Override
        public int[] getLayoutParams() {
            return new int[]{ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT};
        }
    }
}
