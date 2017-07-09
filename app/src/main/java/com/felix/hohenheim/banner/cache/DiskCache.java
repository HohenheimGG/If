package com.felix.hohenheim.banner.cache;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DiskCache implements ImageCache {
    private static volatile DiskCache cache;
    private DiskLruCache bitmapCache;

    static ImageCache getInstance() {
        if(cache == null)
            synchronized (DiskCache.class) {
                if(cache == null)
                    cache = new DiskCache();
            }
        return cache;
    }

    private DiskCache() {
        init();
    }

    private void init() {
        try {
            File dir = ImageCacheParams.getDiskDir();
            if(!dir.exists()) {
                dir.mkdirs();
            }
            bitmapCache = DiskLruCache.open(dir, ImageCacheParams.getVersionCode(), 1, ImageCacheParams.DEFAULT_DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        String cacheKey = hashKey(key);
        try {
            DiskLruCache.Editor editor = bitmapCache.edit(cacheKey);
            if(editor == null) {
                return;
            }
            OutputStream outputStream = editor.newOutputStream(0);
            bitmap.compress(ImageCacheParams.DEFAULT_COMPRESS_FORMAT, ImageCacheParams.DEFAULT_COMPRESS_QUALITY, outputStream);
            editor.commit();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String key) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        String cacheKey = hashKey(key);
        try {
            DiskLruCache.Snapshot snapshot = bitmapCache.get(cacheKey);
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
        return bitmap;
    }

    @Override
    public void clear() {
        try {
            bitmapCache.delete();
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void remove(String key) {
        String cacheKey = hashKey(key);
        try {
            bitmapCache.remove(cacheKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long size() {
        return bitmapCache.size();
    }

    private String hashKey(String key) {
        String cacheKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = byteToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String byteToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for(byte b: bytes) {
            String hex = Integer.toHexString(0XFF & b);
            if(hex.length() == 1)
                builder.append("0");
            builder.append(hex);
        }
        return builder.toString();
    }
}

