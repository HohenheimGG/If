package com.hohenheim.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;
import android.content.res.Resources;
import com.hohenheim.image.memoryCache.ImageCache;
import com.hohenheim.image.memoryCache.MemoryCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageResize {

    private static final String TAG = ImageResize.class.getSimpleName();

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth) {
            inSampleSize *= 2;
            while(height / inSampleSize > reqHeight && width / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }

        int totalPixels = width * height / inSampleSize;
        int totalReqPixels = reqHeight * reqWidth * 2;
        while(totalPixels > totalReqPixels) {
            inSampleSize *= 2;
            totalPixels /= 2;
        }
        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromRes(Resources resource, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resource, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resource, resId, options);
    }

    public static Bitmap decodeBitmapFromByte(byte[] bytes, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        addOption(MemoryCache.getInstance(1), options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static Bitmap decodeRoundBitmapFromByte(byte[] bytes, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        addOption(MemoryCache.getInstance(1), options);
        options.inJustDecodeBounds = false;
        Bitmap source = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        return source;//getRoundedCornerBitmap(source);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            float roundPx = 30;

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setXfermode(null);

            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


            canvas.drawBitmap(bitmap, rect, rectF, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    public static Bitmap decodeBitmapFromFile(File file, ImageView imageView, ImageCache cache) {
        return decodeBitmapFromFile(file, imageView.getWidth(), imageView.getHeight(), cache);
    }

    public static Bitmap decodeBitmapFromFile(File file, int reqWidth, int reqHeight, ImageCache cache) {
        if(!file.exists()) {
            return null;
        }
        Bitmap bitmap = null;
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            bitmap = decodeBitmapFromDescriptor(stream.getFD(), reqWidth, reqHeight, cache);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
        } finally {
            CloseUtil.close(stream);
        }
        return bitmap;
    }

    public static Bitmap decodeBitmapFromDescriptor(FileDescriptor descriptor, int reqWidth, int reqHeight, ImageCache cache) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(descriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        if(cache != null && MemoryCache.class.isInstance(cache)) {
            addOption(cache, options);
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(descriptor, null, options);
    }

    private static void addOption(ImageCache cache, BitmapFactory.Options options) {
        options.inMutable = true;
        Bitmap bitmap = ((MemoryCache)cache).getIntBitmap(options);
        if(bitmap != null) {
            options.inBitmap = bitmap;
        }
    }

    public static Bitmap decodeZBitmapFromFile(File file) {
        if(!file.exists())
            return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        FileInputStream stream;
        try{
            stream = new FileInputStream(file);
            FileDescriptor descriptor = stream.getFD();
            BitmapFactory.decodeFileDescriptor(descriptor, null, options);
            int sampleSize = (int) (options.outHeight / (float) 200);
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(descriptor, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
