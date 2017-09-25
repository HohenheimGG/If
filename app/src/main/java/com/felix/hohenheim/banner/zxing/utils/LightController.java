package com.felix.hohenheim.banner.zxing.utils;

import android.annotation.TargetApi;
import android.content.Context;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.felix.hohenheim.banner.utils.VersionUtil;

import java.lang.annotation.Target;

/**
 * Created by hohenheim on 17/9/24.
 */

public class LightController {

    private CameraManager manager;
    private String cameraId = "0";
    private PackageManager packageManager;
    private Camera camera;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LightController(Context context) {
        manager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        packageManager = context.getPackageManager();
    }

    public void openCamera() {
        boolean hasFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(!hasFlash)
            return ;

        if(VersionUtil.hasM()) {
            try {
                manager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
                openCameraInLowVersion();
            }
        } else {
            openCameraInLowVersion();
        }
    }

    private void openCameraInLowVersion() {
        if (null == camera) {
            camera = Camera.open();
        }
        final Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    public void closeCamera() {
        if(VersionUtil.hasM()) {
            try {
                manager.setTorchMode(cameraId, false);
            } catch(Exception e) {
                e.printStackTrace();
                closeCameraInLowVersion();
            }
        } else {
            closeCameraInLowVersion();
        }
    }

    private void closeCameraInLowVersion() {
        if(camera == null)
            return;
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
