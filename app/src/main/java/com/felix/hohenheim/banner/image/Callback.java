package com.felix.hohenheim.banner.image;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.Resource;

/**
 * Created by hohenheim on 17/7/14.
 */

public interface Callback {

    void onResourceReady(Bitmap bitmap);

    void onException(Exception e);
}
