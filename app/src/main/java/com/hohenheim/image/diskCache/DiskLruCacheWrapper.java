package com.hohenheim.image.diskCache;

import android.graphics.Bitmap;

import com.hohenheim.image.KeyGenerate;
import com.hohenheim.params.ImageCacheParams;
import com.hohenheim.utils.CloseUtil;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by com.hohenheim on 11/07/2017.
 */

public class DiskLruCacheWrapper implements DiskCache {

    private static volatile DiskLruCacheWrapper wrapper;
    private DiskLruCache diskLruCache;
    private KeyGenerate keyGenerate;
    private File dir;
    private int maxValue;
    private DiskCacheLock lock = new DiskCacheLock();

    public static DiskLruCacheWrapper getInstance(File dir, int maxValue) {
        if(wrapper == null)
            synchronized (DiskLruCacheWrapper.class) {
                if(wrapper == null)
                    wrapper = new DiskLruCacheWrapper(dir, maxValue);
            }
            return wrapper;
    }

    private DiskLruCacheWrapper(File dir, int maxValue) {
        this.dir = dir;
        this.maxValue = maxValue;
        this.keyGenerate = new KeyGenerate();
    }

    private synchronized DiskLruCache getCache() throws IOException {
        if(diskLruCache == null)
            diskLruCache = DiskLruCache.open(dir, ImageCacheParams.getVersionCode(), 1, maxValue);
        return diskLruCache;
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
            diskLruCache = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void quietlyAbortUnlessCommitted(DiskLruCache.Editor editor) {
        if(editor != null)
            editor.abortUnlessCommitted();
    }
}
