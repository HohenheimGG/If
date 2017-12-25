package com.hohenheim.scancode.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by hohenheim on 2017/12/25.
 */

public class AnimationHelper {

    public static TranslateAnimation getTopToBottomTranAmimate() {
        TranslateAnimation scanAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.08f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        scanAnimation.setDuration(4500);
        scanAnimation.setRepeatCount(-1);
        scanAnimation.setRepeatMode(Animation.RESTART);
        return scanAnimation;
    }
}
