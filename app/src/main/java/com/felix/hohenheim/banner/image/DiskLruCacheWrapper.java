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
        try {
            DiskLruCache.Editor editor = getCache().edit(cacheKey);
            if(editor == null) {
                return;
            }
            try {
                OutputStream outputStream = editor.newOutputStream(0);
                if (writer.write(outputStream)) {
                    editor.commit();
                }
                outputStream.close();
            } finally {
                editor.abortUnlessCommitted();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File get(String key) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        String cacheKey = keyGenerate.getKey(key);
        try {
            DiskLruCache.Snapshot snapshot = getCache().get(cacheKey);
            if(snapshot == null)
                return null;
            inputStream = snapshot.getInputStream(0);
            if(inputStream == null)
                return null;
            FileDescriptor descriptor = ((FileInputStream)inputStream).getFD();
            bitmap = ImageResize.decodeBitmapFromDescriptor(descriptor, ImageCacheParams.DEFAULT_IMAGE_WIDTH, ImageCacheParams.DEFAULT_IMAGE_HEIGHT, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(inputStream);
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
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized DiskLruCache getCache() {
        if(diskLruCache == null)
            init();
        return diskLruCache;
    }
}
