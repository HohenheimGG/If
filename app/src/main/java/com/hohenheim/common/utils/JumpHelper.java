package com.hohenheim.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by hohenheim on 2017/12/24.
 */

public class JumpHelper {

    public static void openBrowser(Context context, String url, String description) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(Intent.createChooser(intent, description));
    }

    public static void openAlbum(Activity activity, int requestCode, String description) {
        Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
        if (!VersionUtil.hasKitKat()) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_PICK);
        }
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, description);
        activity.startActivityForResult(wrapperIntent, requestCode);
    }
}
