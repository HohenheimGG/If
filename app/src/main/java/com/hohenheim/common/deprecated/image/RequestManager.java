package com.hohenheim.common.deprecated.image;

import android.graphics.Bitmap;

import com.hohenheim.common.deprecated.image.diskCache.DiskCache;
import com.hohenheim.common.deprecated.image.encoder.Encoder;
import com.hohenheim.common.deprecated.image.memoryCache.ImageCache;
import com.hohenheim.common.deprecated.image.request.Request;

import java.util.concurrent.ExecutorService;

/**
 * Created by com.hohenheim on 17/7/14.
 */

public class RequestManager {

    private final ExecutorService sourceService;
    private final ExecutorService diskCacheService;
    private final ImageCache memoryCache;
    private final DiskCache.Factory diskCacheFactory;

    public RequestManager(ExecutorService sourceService, ExecutorService diskCacheService, ImageCache memoryCache, DiskCache.Factory diskCacheFactory) {
        this.sourceService = sourceService;
        this.diskCacheService = diskCacheService;
        this.memoryCache = memoryCache;
        this.diskCacheFactory = diskCacheFactory;
    }

    public void load(Decoder decoder, Encoder encoder, String url, Request request) {
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap != null) {
            request.onResourceReady(bitmap);
            request.recycler();
            return;
        }

    }

}
