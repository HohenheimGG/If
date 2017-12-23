package com.hohenheim.common.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.hohenheim.BuildConfig;
import com.hohenheim.common.db.DBString;
import com.hohenheim.common.manager.DBModuleManager;
import com.hohenheim.homepage.db.RecentDBInterface;
import com.hohenheim.scancode.db.HistoryDBInterface;

import java.lang.ref.WeakReference;


public class IfApplication extends Application{
    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = new WeakReference<>(getApplicationContext());

        /**
         * 注册数据库
         */
        registerDB();
        /**
         * 初始化数据库
         */
        DBModuleManager.getInstance().initDB(this, DBString.DB_NAME, null, BuildConfig.VERSION_CODE);
    }

    private void registerDB() {
        DBModuleManager.getInstance().registerModuleInterface(new HistoryDBInterface());
        DBModuleManager.getInstance().registerModuleInterface(new RecentDBInterface());
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
