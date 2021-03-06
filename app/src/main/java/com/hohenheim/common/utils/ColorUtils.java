package com.hohenheim.common.utils;

import android.animation.ArgbEvaluator;

/**
 * Created by hohenheim on 17/12/1.
 */

public class ColorUtils {

    private static ArgbEvaluator evaluator = new ArgbEvaluator();

    /**
     * 颜色变化过度
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public static int evaluateColor(double fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int)(fraction * (endA - startA))) << 24) |
                ((startR + (int)(fraction * (endR - startR))) << 16) |
                ((startG + (int)(fraction * (endG - startG))) << 8) |
                ((startB + (int)(fraction * (endB - startB))));
    }
}
