package com.felix.hohenheim.banner.image;

import android.content.Context;

import com.felix.hohenheim.banner.image.diskCache.DiskCache;
import com.felix.hohenheim.banner.image.memoryCache.ImageCache;
import com.felix.hohenheim.banner.image.request.Request;

import java.util.concurrent.ExecutorService;


/**
 * Created by hohenheim on 13/07/2017.
 */

public class ImageLoader {

    private static volatile ImageLoader loader;

    private final ImageCache memoryCache;
    private final RequestManager manager;

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

    ImageLoader(ImageCache memoryCache, RequestManager manager) {
        this.memoryCache = memoryCache;
        this.manager = manager;
    }

//    public Request load() {
//
//    }
}
