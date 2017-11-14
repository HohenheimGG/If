package com.hohenheim.image.encoder;

import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;

import java.io.OutputStream;

/**
 * Created by com.hohenheim on 13/07/2017.
 */

public class BitmapEncoder implements Encoder<Bitmap> {
    private static final String TAG = "BitmapEncoder";
    private static final int DEFAULT_COMPRESSION_QUALITY = 90;
    private Bitmap.CompressFormat compressFormat;
    private int quality;

    public BitmapEncoder() {
        this(null, DEFAULT_COMPRESSION_QUALITY);
    }

    public BitmapEncoder(Bitmap.CompressFormat compressFormat, int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    @Override
    public boolean encode(Bitmap bitmap, OutputStream os) {
        long start = LogTime.getLogTime();
        Bitmap.CompressFormat format = getFormat(bitmap);
        bitmap.compress(format, quality, os);
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Compressed with type: " + format + " of size " + Util.getBitmapByteSize(bitmap) + " in "
                    + LogTime.getElapsedMillis(start));
        }
        return true;
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap) {
        if (compressFormat != null) {
            return compressFormat;
        } else if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }
}
