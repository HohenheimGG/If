package com.felix.hohenheim.banner.image;

import android.content.Context;


/**
 * Created by hohenheim on 13/07/2017.
 */

public class ImageLoader {

    private static volatile ImageLoader loader;

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
}
