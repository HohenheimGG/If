package com.hohenheim.scancode.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.zxing.Result;
import com.hohenheim.R;
import com.hohenheim.scancode.activity.CaptureActivity;
import com.hohenheim.scancode.camera.CameraManager;

import java.io.IOException;

/**
 * Created by hohenheim on 2017/12/25.
 * 辅助CaptureActivity
 */

public class CaptureHelper {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    public static void initCamera(SurfaceHolder surfaceHolder, CameraManager cameraManager)
            throws IOException, RuntimeException {

        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            throw e;
        }
    }

    public static void decodeAlbum(String albumPath, CaptureActivityHandler handler) {
        if(TextUtils.isEmpty(albumPath))
            return;
        Message msg = Message.obtain();
        msg.what = R.id.decode_path;
        Bundle bundle = new Bundle();
        bundle.putString("path", albumPath);
        msg.setData(bundle);
        handler.getDecodeHandler().sendMessage(msg);
    }

    public static void cameraPause(InactivityTimer timer, BeepManager beep, CameraManager cameraManager) {
        timer.onPause();
        beep.close();
        cameraManager.closeDriver();
    }

    public static void onDestroy(InactivityTimer timer, BeepManager beep) {
        timer.shutdown();
        beep.shutdown();
    }
}
