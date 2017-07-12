package com.felix.hohenheim.banner.image;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hohenheim on 11/07/2017.
 */

public interface DiskCache {

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
