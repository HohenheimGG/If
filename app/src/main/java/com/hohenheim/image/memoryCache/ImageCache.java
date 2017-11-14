package com.hohenheim.image.memoryCache;

import android.graphics.Bitmap;

public interface ImageCache {

    void put(String key, Bitmap bitmap);
    Bitmap get(String key);
    void remove(String key);
    void clear();
    long size();//以MiB为单位

}
