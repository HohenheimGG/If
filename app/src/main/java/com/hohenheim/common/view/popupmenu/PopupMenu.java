package com.hohenheim.common.view.popupmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
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
        rvView.addItemDecoration(new DividerItemDecoration(context));
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

    private static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };

        private Drawable mDivider;
        private final Rect mBounds = new Rect();

        /**
         * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
         * {@link LinearLayoutManager}.
         *
         * @param context Current context, it will be used to access resources.
         */
        public DividerItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }


        /**
         * Sets the {@link Drawable} for this divider.
         *
         * @param drawable Drawable that should be used as a divider.
         */
        public void setDrawable(Drawable drawable) {
            mDivider = drawable;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (parent.getLayoutManager() == null) {
                return;
            }
            drawVertical(c, parent);
        }

        @SuppressLint("NewApi")
        private void drawVertical(Canvas canvas, RecyclerView parent) {
            canvas.save();
            final int left;
            final int right;
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft()+40;
                right = parent.getWidth() - parent.getPaddingRight()-40;
                canvas.clipRect(left, parent.getPaddingTop(), right,
                        parent.getHeight() - parent.getPaddingBottom());
            } else {
                left=40;
                right = parent.getWidth()-40;
            }

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount-1; i++) {
                final View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
//            final int top = bottom - mDivider.getIntrinsicHeight();
                final int top =bottom-2;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            canvas.restore();
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}