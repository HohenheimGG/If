package com.felix.hohenheim.banner.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by hohenheim on 17/9/20.
 */

public class PermissionUtils {


    public static void needPermission(Context context, int requestCode, String[] permissions) {
        requestPermission(context, requestCode, permissions);
    }


    private static void requestPermission(Object object, int requestCode, String[] permissions) {
        if(Build.VERSION.SDK_INT < 23) {

        }
    }
}
