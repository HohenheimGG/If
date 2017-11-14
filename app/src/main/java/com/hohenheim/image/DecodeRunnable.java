package com.hohenheim.image;

import com.hohenheim.image.diskCache.DiskCache;
import com.hohenheim.image.encoder.Encoder;
import com.hohenheim.image.memoryCache.ImageCache;
import com.hohenheim.image.request.Request;

/**
 * Created by com.hohenheim on 17/7/14.
 */

public class DecodeRunnable implements Runnable {

    private Decoder decoder;
    private Encoder encoder;
    private ImageCache memoryCache;
    private DiskCache diskCache;
    private Request request;
    private String url;

    public DecodeRunnable(Decoder decoder,
                          Encoder encoder,
                          ImageCache memoryCache,
                          DiskCache.Factory diskCacheFactory,
                          Request request, String url) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.memoryCache = memoryCache;
        this.diskCache = diskCacheFactory.build();
        this.request = request;
        this.url = url;
    }

    @Override
    public void run() {

    }

    private void decodeFromDiskCache() {


    }
}
