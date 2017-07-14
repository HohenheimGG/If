package com.felix.hohenheim.banner.image.diskCache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by hohenheim on 13/07/2017.
 */

public class DiskCacheFactory implements DiskCache.Factory {

    private int maxValue;
    private File cacheDir;

    public DiskCacheFactory(Context context) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
    }

    public DiskCacheFactory(Context context, int maxValue) {
        this(context, maxValue, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
    }

    public DiskCacheFactory(Context context, int maxValue, String dir) {
        File cachePath = context.getCacheDir();
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && context.getExternalCacheDir() != null)
            cacheDir = context.getExternalCacheDir();
        this.cacheDir = new File(cachePath.getPath() + File.separator + dir);
        this.maxValue = maxValue;
    }

    public File getCacheDir() {
        return cacheDir;
    }

    @Override
    public DiskCache build() {
        if (cacheDir == null) {
            return null;
        }

        if (!cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory())) {
            return null;
        }

        return DiskLruCacheWrapper.getInstance(cacheDir, maxValue);
    }
}
