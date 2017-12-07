package com.hohenheim.common.deprecated.image.diskCache;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by com.hohenheim on 11/07/2017.
 */

public interface DiskCache {

    interface Factory {
        int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;//250MB
        String DEFAULT_DISK_CACHE_DIR = "Hohenheim";

        DiskCache build();
    }

    interface Write {
        boolean write(OutputStream stream);
    }

    interface Read {
        Bitmap read(InputStream inputStream);
    }

    void put(String key, Write write);
    Bitmap get(String key, Read read);
    void delete(String key);
    void clear();
}
