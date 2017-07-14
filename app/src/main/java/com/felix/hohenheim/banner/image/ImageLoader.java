package com.felix.hohenheim.banner.image;

import android.content.Context;

import java.util.concurrent.ExecutorService;


/**
 * Created by hohenheim on 13/07/2017.
 */

public class ImageLoader {

    private static volatile ImageLoader loader;

    private final ExecutorService sourceService;
    private final ExecutorService diskCacheService;
    private final ImageCache memoryCache;
    private final DiskCache.Factory diskCacheFactory;

    public static ImageLoader get(Context context) {
        if(loader == null)
            synchronized (ImageLoader.class) {
                if(loader == null) {
                    ImageLoaderBuilder builder = new ImageLoaderBuilder(context);
                    loader = builder.build();
                }
            }
        return loader;
    }

    ImageLoader(ExecutorService sourceService, ExecutorService diskCacheService, ImageCache memoryCache, DiskCache.Factory diskCacheFactory) {
        this.sourceService = sourceService;
        this.diskCacheService = diskCacheService;
        this.memoryCache = memoryCache;
        this.diskCacheFactory = diskCacheFactory;
    }
}
