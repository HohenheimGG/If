package com.hohenheim.common.deprecated.image;

import android.graphics.Bitmap;

/**
 * Created by com.hohenheim on 17/7/14.
 */

public interface Callback {

    void onResourceReady(Bitmap bitmap);

    void onException(Exception e);
}
