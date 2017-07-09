package com.felix.hohenheim.banner.loader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.felix.hohenheim.banner.cache.DoubleCache;
import com.felix.hohenheim.banner.cache.ImageCache;
import com.felix.hohenheim.banner.network.ImageRequest;
import com.felix.hohenheim.banner.network.NetworkManager;
import com.felix.hohenheim.banner.network.Response;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ImageLoader {

    private static Executor loader = Executors.newSingleThreadExecutor();

    private static ImageCache cache = DoubleCache.getInstance();

    public static void setCache(ImageCache cache) {
        ImageLoader.cache = cache;
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void load(final String key, final ImageView imageView) {
        imageView.setTag(key);
        loader.execute(new ReadRunnable(key, imageView));
    }

    private static class ReadRunnable implements Runnable{

        private String key;
        private ImageView imageView;

        public ReadRunnable(String key, ImageView imageView) {
            this.key = key;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            Bitmap bitmap = cache.get(key);
            if(!imageView.getTag().equals(key))
                return;
            if(bitmap != null) {
                handler.post(new UIRunnable(imageView, bitmap));
                return;
            }
            Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    if(bitmap == null) {
                        onError(new NullPointerException());
                        return;
                    }
                    if(imageView.getTag().equals(key))
                        imageView.setImageBitmap(bitmap);
                    loader.execute(new WriteRunnable(key, bitmap));
                }

                @Override
                public void onError(Exception e) {

                }
            };
            NetworkManager.getInstance().executor(new ImageRequest(listener, key, imageView.getWidth(), imageView.getHeight()));
        }
    }

    private static class WriteRunnable implements Runnable {

        private String key;
        private Bitmap bitmap;

        public WriteRunnable(String key, Bitmap bitmap) {
            this.key = key;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            cache.put(key, bitmap);
        }
    }

    private static class UIRunnable implements Runnable {
        private ImageView view;
        private Bitmap bitmap;

        public UIRunnable(ImageView view, Bitmap bitmap){
            this.view = view;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            view.setImageBitmap(bitmap);
        }
    }
}
