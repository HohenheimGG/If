package com.felix.hohenheim.banner.image.memoryCache;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

/**
 * Created by hohenheim on 14/07/2017.
 */

public class MemorySizeCalculator {

    private static final int BYTES_PER_ARGB_8888_PIXEL = 4;
    private static final int MEMORY_CACHE_TARGET_SCREENS = 2;
    private static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33f;

    private final int memoryCacheSize;
    private final Context context;

    public MemorySizeCalculator(Context context) {
        this.context = context;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int maxSize = getMaxSize(activityManager);
        final int screenSize = metrics.widthPixels * metrics.heightPixels * BYTES_PER_ARGB_8888_PIXEL;

        int targetMemoryCacheSize = screenSize * MEMORY_CACHE_TARGET_SCREENS;

        if(targetMemoryCacheSize >= maxSize)
            memoryCacheSize = maxSize;
        else
            memoryCacheSize = targetMemoryCacheSize;
    }

    public int getMemoryCacheSize() {
        return memoryCacheSize;
    }

    private String toMb(int bytes) {
        return Formatter.formatFileSize(context, bytes);
    }

    private static int getMaxSize(ActivityManager activityManager) {
        final int memoryClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
        return Math.round(memoryClassBytes * LOW_MEMORY_MAX_SIZE_MULTIPLIER);
    }
}
