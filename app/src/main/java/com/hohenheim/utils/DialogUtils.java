package com.hohenheim.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.hohenheim.R;


/**
 * Created by com.hohenheim on 2017/9/20.
 */

public class DialogUtils {

    public static void showPermissionDialog(final Activity activity) {
        if(activity == null)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.app_name));
        builder.setMessage("相机打开出错，请检查是否开启相机权限");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                activity.finish();
            }
        });
        builder.show();
    }

    public static void showLeadingDialog(final Activity activity,
                                         String content,
                                         DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity).setMessage(content)
                .setPositiveButton("确定", okListener).create().show();
    }
}
