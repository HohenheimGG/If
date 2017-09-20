package com.felix.hohenheim.banner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by hohenheim on 17/9/20.
 */

public class PermissionUtils {

    public static void requestCameraPermission(Activity activity) {
        if(Build.VERSION.SDK_INT < 23) {
            if(!cameraIsCanUse())
                DialogUtils.showPermissionDialog(activity);
        } else {
            checkPermission(activity, "android.permission.CAMERA", 1);
        }
    }

    private static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    private static boolean checkPermission(final Activity activity, final String permission, final int requestCode) {
        //检查权限
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //显示我们自定义的一个窗口引导用户开启权限
                DialogUtils.showLeadingDialog(activity, "请允许开启相机权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                    }
                });
            } else {
                //申请权限
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
            return false;
        } else {  //已经拥有权限
            return true;
        }
    }

}
