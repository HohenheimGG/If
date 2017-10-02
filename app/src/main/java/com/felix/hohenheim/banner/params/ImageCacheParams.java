package com.felix.hohenheim.banner.params;

import android.graphics.Bitmap;

import com.felix.hohenheim.banner.IfApplication;

import java.io.File;


public class ImageCacheParams {

    public static final int DEFAULT_MEMORY_CACHE_SIZE = 10 * 1024 * 1024;//10MiB
    public static final int DEFAULT_DISK_CACHE_SIZE = 20 * 1024 * 1024;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final int DEFAULT_COMPRESS_QUALITY = 70;
    public static final int DEFAULT_IMAGE_WIDTH = 800;
    public static final int DEFAULT_IMAGE_HEIGHT = 800;
    public static final String DEFAULT_DISK_NAME = "Hohenheim";

    /**
     * 获取SD卡地址
     * @return
     */
    public static File getDiskDir() {
        return new File(IfApplication.getSDCardDir() + File.separator + DEFAULT_DISK_NAME);
    }

    public static int getVersionCode() {
        return IfApplication.getVersion();
    }
}
