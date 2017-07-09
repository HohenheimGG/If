package com.felix.hohenheim.banner.cache;

import android.graphics.Bitmap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DoubleCache implements ImageCache{

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
    private static ReentrantReadWriteLock.ReadLock read = lock.readLock();
    private static ReentrantReadWriteLock.WriteLock write = lock.writeLock();

    private ImageCache memory = MemoryCache.getInstance();
    private ImageCache disk = DiskCache.getInstance();

    private static volatile DoubleCache cache;

    public static DoubleCache getInstance() {
        if(cache == null)
            synchronized (DoubleCache.class) {
                if(cache == null)
                    cache = new DoubleCache();
            }
            return cache;
    }

    private DoubleCache() {}

    @Override
    public void put(String key, Bitmap bitmap) {
        try{
            write.lock();
            memory.put(key, bitmap);
            disk.put(key, bitmap);
        } finally {
            write.unlock();
        }
    }

    @Override
    public Bitmap get(String key) {
        try {
            read.lock();
            Bitmap bitmap = memory.get(key);
            if (bitmap == null)
                bitmap = disk.get(key);
            return bitmap;
        }finally {
            read.unlock();
        }
    }

    @Override
    public void remove(String key) {
        try{
            write.lock();
            memory.remove(key);
            disk.remove(key);
        }finally {
            write.unlock();
        }
    }

    @Override
    public void clear() {
        try{
            write.lock();
            memory.clear();
            disk.clear();
        } finally {
            write.unlock();
        }
    }

    @Override
    public long size() {
        return 0;
    }
}
