package com.felix.hohenheim.banner.utils;

import android.content.Context;

import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

/**
 * Created by hohenheim on 17/9/24.
 */

public class LightController {

    private CameraManager manager;
    private PackageManager packageManager;
    private Camera camera;

    public LightController(Context context) {
        manager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
        packageManager = context.getPackageManager();
    }

    public void openCamera() {
        if(VersionUtil.hasM()) {
            try {
                manager.setTorchMode("0", true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            final FeatureInfo[] features = packageManager.getSystemAvailableFeatures();
            for (final FeatureInfo f : features) {
                if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                    if (null == camera) {
                        camera = Camera.open();
                    }
                    final Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                }
            }
        }
    }

    public void closeCamera() {
        if(VersionUtil.hasM()) {
            try {
                manager.setTorchMode("0", false);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            if(camera == null)
                return;
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
