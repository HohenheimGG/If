package com.felix.hohenheim.banner;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.lang.ref.WeakReference;


public class BannerApplication extends Application{
    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());
    }

    public static Context getContext() {
        return context.get();
    }

    //获取SD卡地址
    public static String getSDCardDir() {
        String dir;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = getContext().getExternalCacheDir().getPath();
        } else {
            dir = getContext().getCacheDir().getPath();
        }
        return dir;
    }

    public static int getVersion() {
        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }



}
