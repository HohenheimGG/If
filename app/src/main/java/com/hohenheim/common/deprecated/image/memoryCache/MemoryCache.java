package com.hohenheim.common.deprecated.image.memoryCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.hohenheim.common.utils.VersionUtil;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MemoryCache implements ImageCache {

    private static volatile MemoryCache cache;
    private LruCache<String, Bitmap> lruCache;
    private final Set<SoftReference<Bitmap>> reuseSet = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());

    public static ImageCache getInstance(int cacheSize) {
        if(cache == null)
            synchronized (MemoryCache.class) {
                if(cache == null) {
                    cache = new MemoryCache(cacheSize);
                }
            }
        return cache;
    }

    private MemoryCache(int cacheSize) {
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                reuseSet.add(new SoftReference<>(oldValue));
            }

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();//等同于value.getRowByte() * value.getHeight()
            }
        };
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    @Override
    public Bitmap get(String key) {
        return lruCache.get(key);
    }

    @Override
    public void remove(String key) {
        lruCache.remove(key);
    }

    @Override
    public void clear() {
        lruCache.evictAll();
    }

    @Override
    public long size() {
        return lruCache.size();
    }

    public Bitmap getIntBitmap(BitmapFactory.Options options) {
        if(reuseSet.size() == 0) {
            return null;
        }
        Bitmap bitmap = null;
        synchronized (reuseSet) {
            final Iterator<SoftReference<Bitmap>> iterator = reuseSet.iterator();
            while(iterator.hasNext()) {
                SoftReference<Bitmap> reference = iterator.next();
                Bitmap item = reference.get();
                if(item == null || !item.isMutable()) {
                    iterator.remove();
                    continue;
                }

                if(canUseInBitmap(options, item)) {
                    bitmap = item;
                    iterator.remove();
                    break;
                }
            }
        }
        return bitmap;
    }

    private boolean canUseInBitmap(BitmapFactory.Options options, Bitmap bitmap) {
        if(!VersionUtil.hasKitKat()) {
            return bitmap.getHeight() == options.outHeight &&
                    bitmap.getWidth() == options.outWidth &&
                    options.inSampleSize == 1;
        }
        int width = options.outWidth / options.inSampleSize;
        int height = options.outHeight / options.inSampleSize;
        int byteCount = width * height * getBytesPerPixel(bitmap.getConfig());
        return bitmap.getAllocationByteCount() >= byteCount;
    }

    private int getBytesPerPixel(Bitmap.Config config) {
        if(config == null)
            return 1;
        else if(config == Bitmap.Config.ARGB_8888)
            return 4;
        else if(config == Bitmap.Config.ARGB_4444)
            return 2;
        else if(config == Bitmap.Config.RGB_565)
            return 2;
        else if(config == Bitmap.Config.RGB_565)
            return 1;
        return 1;
    }
}
