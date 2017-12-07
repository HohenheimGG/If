package com.hohenheim.common.transformer;

import android.view.View;

public class ScalePageTransformer implements BasePageTransformer {

    private float scale;

    public ScalePageTransformer() {
        this(DEFAULT_SCALE);
    }

    public ScalePageTransformer(float scale) {
        this.scale = scale;
    }

    @Override
    public void transformPage(View page, float position) {

        int width = page.getWidth();
        int height = page.getHeight();

        //缩放的中点
        page.setPivotX(width / 2);
        page.setPivotY(height / 2);

        float curScale;
        if (position < -1) {//view在左侧, 缩放停止.
            curScale = scale;
        } else if (position >= -1 && position < 0) { //view从中间往左滑
            curScale = (position + 1) * (1 - scale) + scale;
        } else if (position >= 0 && position <= 1) { //view从右往中间滑
            curScale = (1 - position) * (1 - scale) + scale;
        } else { //view在右侧, 缩放停止
            curScale = scale;
        }
        setScale(page, curScale);
    }

    private void setScale(View page, float scale) {
        page.setScaleX(scale);
        page.setScaleY(scale);
    }
}
