package com.hohenheim.common.image;

import android.content.Context;

import com.hohenheim.common.image.diskCache.DiskCache;
import com.hohenheim.common.image.diskCache.DiskCacheFactory;
import com.hohenheim.common.image.memoryCache.ImageCache;
import com.hohenheim.common.image.memoryCache.MemoryCache;
import com.hohenheim.common.image.memoryCache.MemorySizeCalculator;

import java.util.concurrent.ExecutorService;


/**
 * Created by com.hohenheim on 13/07/2017.
 */

public final class ImageLoaderBuilder {
    private final Context context;
    private ImageCache memoryCache;
    private ExecutorService sourceService;
    private ExecutorService diskCacheService;
    private DiskCache.Factory diskCacheFactory;

    public ImageLoaderBuilder(Context context) {
        this.context = context.getApplicationContext();
    }

    public ImageLoaderBuilder setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
        return this;
    }

    public ImageLoaderBuilder setSourceService(ExecutorService sourceService) {
        this.sourceService = sourceService;
        return this;
    }

    public ImageLoaderBuilder setDiskCacheService(ExecutorService diskCacheService) {
        this.diskCacheService = diskCacheService;
        return this;
    }

    public ImageLoaderBuilder setDiskCacheFactory(DiskCache.Factory diskCacheFactory) {
        this.diskCacheFactory = diskCacheFactory;
        return this;
    }

    public ImageLoader build() {
        if (sourceService == null) {
            final int cores = Math.max(1, Runtime.getRuntime().availableProcessors());
            sourceService = new ImageThreadPoolExecutor(cores);
        }
        if (diskCacheService == null) {
            diskCacheService = new ImageThreadPoolExecutor(1);
        }

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);

        if (memoryCache == null) {
            memoryCache = MemoryCache.getInstance(calculator.getMemoryCacheSize());
        }

        if (diskCacheFactory == null) {
            diskCacheFactory = new DiskCacheFactory(context);
        }

        RequestManager manager = new RequestManager(sourceService, diskCacheService, memoryCache, diskCacheFactory);

        return new ImageLoader(memoryCache, manager);
    }
}
