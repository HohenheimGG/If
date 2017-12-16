package com.hohenheim.common.view.popupmenu;
;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by hohenheim on 2017/12/14.
 */

/**
 * Created by hohenheim on 17/12/14.
 */

public class PopupMenu {

    private SparseArrayCompat<PopupWindow.OnDismissListener> mDismissListener = new SparseArrayCompat<>();
    private int alphaListenerPosition;
    private PopupWindow popupWindow;

    public PopupMenu build(MenuBaseAdapter adapter) {
        if(adapter == null)
            throw new IllegalArgumentException("adapter wat null, please check the argument");

        popupWindow = new PopupWindow(adapter.getView(),
                adapter.getLayoutParams()[0],
                adapter.getLayoutParams()[1]);
        if(adapter.getBackgroundDrawable() != null)
            popupWindow.setBackgroundDrawable(adapter.getBackgroundDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                for(int i = 0; i < mDismissListener.size(); i ++)
                    mDismissListener.get(i).onDismiss();
            }
        });
        adapter.init(popupWindow);
        return this;
    }

    /**
     * 弹出PopupMenu
     * @param anchor
     */
    public void show(View anchor) {
        if(popupWindow != null)
            popupWindow.showAsDropDown(anchor);
    }

    public void dismiss() {
        if(popupWindow != null)
            popupWindow.dismiss();
    }

    /**
     * 弹出PopupMenu, 修改背景.
     * @param anchor
     * @param window
     */
    public void showWithWindowAlphaChange(View anchor, final Window window) {
        show(anchor);
        //虚化背景
        changeWindowAlpha(0.7f, window);
        addOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复背景
                changeWindowAlpha(1.0f, window);
                removeOnDismissListener(alphaListenerPosition);
            }
        });
        alphaListenerPosition = mDismissListener.size() - 1;
    }

    private void changeWindowAlpha(float alpha, Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
    }

    public void addOnDismissListener(PopupWindow.OnDismissListener dismissListener) {
        this.mDismissListener.append(this.mDismissListener.size(), dismissListener);
    }

    public void removeOnDismissListener(int index) {
        if(index > this.mDismissListener.size() || index < 0)
            return;
        this.mDismissListener.delete(index);
    }

    public void clearOnDismisslistener() {
        this.mDismissListener.clear();
    }
}