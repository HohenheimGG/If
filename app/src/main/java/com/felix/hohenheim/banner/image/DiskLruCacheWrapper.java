package com.felix.hohenheim.banner.image;

import android.graphics.Bitmap;

import com.felix.hohenheim.banner.params.ImageCacheParams;
import com.felix.hohenheim.banner.utils.CloseUtil;
import com.felix.hohenheim.banner.utils.ImageResize;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hohenheim on 11/07/2017.
 */

public class DiskLruCacheWrapper implements DiskCache {

    private static volatile DiskLruCacheWrapper wrapper;
    private DiskLruCache diskLruCache;
    private KeyGenerate keyGenerate;
    private DiskCacheLock lock = new DiskCacheLock();

    public static DiskLruCacheWrapper getInstance() {
        if(wrapper == null)
            synchronized (DiskLruCacheWrapper.class) {
                if(wrapper == null)
                    wrapper = new DiskLruCacheWrapper();
            }
            return wrapper;
    }

    private DiskLruCacheWrapper() {
        init();
        keyGenerate = new KeyGenerate();
    }

    private void init() {
        try {
            File dir = ImageCacheParams.getDiskDir();
            if(!dir.exists()) {
                dir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(dir, ImageCacheParams.getVersionCode(), 1, ImageCacheParams.DEFAULT_DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(String key, Write writer) {
        String cacheKey = keyGenerate.getKey(key);
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        lock.acquire(key);
        try {
            editor = getCache().edit(cacheKey);
            if(editor == null) {
                return;
            }
            outputStream = editor.newOutputStream(0);
            if (outputStream != null && writer.write(outputStream)) {
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(outputStream);
            quietlyAbortUnlessCommitted(editor);
            lock.release(key);
        }
    }

    @Override
    public Bitmap get(String key, Read read) {
        String cacheKey = keyGenerate.getKey(key);
        try {
            DiskLruCache.Snapshot snapshot = getCache().get(cacheKey);
            if(snapshot == null)
                return null;
            return read.read(snapshot.getInputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String key) {
        String cacheKey = keyGenerate.getKey(key);
        try {
            getCache().remove(cacheKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void clear() {
        try {
            getCache().delete();
            init();//重置
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized DiskLruCache getCache() {
        if(diskLruCache == null)
            init();
        return diskLruCache;
    }

    private void quietlyAbortUnlessCommitted(DiskLruCache.Editor editor) {
        if(editor != null)
            editor.abortUnlessCommitted();
    }
}
