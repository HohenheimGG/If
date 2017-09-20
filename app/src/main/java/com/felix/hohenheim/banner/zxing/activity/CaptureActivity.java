/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.felix.hohenheim.banner.zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.felix.hohenheim.banner.R;
import com.felix.hohenheim.banner.adapter.PopWindowAdapter;
import com.felix.hohenheim.banner.utils.PermissionUtils;
import com.felix.hohenheim.banner.view.ScanPopWindow;
import com.felix.hohenheim.banner.zxing.camera.CameraManager;
import com.felix.hohenheim.banner.zxing.decode.DecodeThread;
import com.felix.hohenheim.banner.zxing.utils.BeepManager;
import com.felix.hohenheim.banner.zxing.utils.CaptureActivityHandler;
import com.felix.hohenheim.banner.zxing.utils.InactivityTimer;

import com.google.zxing.Result;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final int SELECT_CODE = 1;
    private boolean isHasSurface = false;
    private String scanResult;
    private String albumPath = null;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private TranslateAnimation scanAnimation;
    private ClipboardManager clipboard;

    private SurfaceView scanPreview;
    private ImageView scanLine;
    private RelativeLayout cropView;
    private LinearLayout parent;
    private ScanPopWindow scanWindow;
    private PopWindowAdapter scanAdapter;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_capture);

        parent = (LinearLayout)findViewById(R.id.capture);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        cropView = (RelativeLayout)findViewById(R.id.capture_crop_view);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        initNavBar();
        initAnimation();
        initPopWindow();
    }

    /**
     * 初始化标题栏
     */
    private void initNavBar() {
        RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        titleLayout.findViewById(R.id.iv_title_back_button).setVisibility(View.GONE);

        Button albumBtn = (Button)titleLayout.findViewById(R.id.btn_title_left_button);
        albumBtn.setVisibility(View.VISIBLE);
        albumBtn.setText("相册");
        albumBtn.setOnClickListener(this);

        Button historyBtn = (Button)titleLayout.findViewById(R.id.btn_title_right_button);
        historyBtn.setVisibility(View.VISIBLE);
        historyBtn.setText("历史记录");
        historyBtn.setOnClickListener(this);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        scanAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.08f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        scanAnimation.setDuration(4500);
        scanAnimation.setRepeatCount(-1);
        scanAnimation.setRepeatMode(Animation.RESTART);
    }

    private ScanPopWindow initPopWindow() {
        if(scanWindow == null) {
            scanWindow = new ScanPopWindow(this);
            scanAdapter = new PopWindowAdapter(this, this);
            scanWindow.setAdapter(scanAdapter);
        }
        return scanWindow;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.top:
                clipboard.setPrimaryClip(ClipData.newPlainText(null, scanResult));
                Toast.makeText(CaptureActivity.this, scanResult + "已复制至粘贴板", Toast.LENGTH_SHORT).show();
                scanWindow.dismiss();
                restartPreviewAfterDelay(1000);
                break;
            case R.id.bottom:
                scanWindow.dismiss();
                restartPreviewAfterDelay(1000);
                openWithBrowser(scanResult);
                break;
            case R.id.btn_title_left_button:
                Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
                if (Build.VERSION.SDK_INT < 19) {
                    innerIntent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    innerIntent.setAction(Intent.ACTION_PICK);
                }
                innerIntent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
                startActivityForResult(wrapperIntent, SELECT_CODE);
                break;
            case R.id.btn_title_right_button:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_CODE:
                    //获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        albumPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraResume();
    }

    private void cameraResume() {
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }
        inactivityTimer.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanLine.startAnimation(scanAnimation);
    }

    @Override
    protected void onPause() {
        cameraPause();
        super.onPause();
        scanLine.clearAnimation();
    }

    private void cameraPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if(isHasSurface)
            return;
        isHasSurface = true;
        initCamera(holder);
    }

    private void decodeAlbumImage() {
        if(albumPath != null) {
            Message msg = Message.obtain();
            msg.what = R.id.decode_path;
            Bundle bundle = new Bundle();
            bundle.putString("path", albumPath);
            bundle.putInt("width", cropView.getWidth());
            bundle.putInt("width", cropView.getWidth());
            msg.setData(bundle);
            handler.getDecodeHandler().sendMessage(msg);
            albumPath = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        if(rawResult != null) {
            scanResult = recode(rawResult.getText());
        } else {
            scanResult = recode(bundle.getString("result", ""));
        }
        scanAdapter.notifyMsg("二维码内容为:\n" + scanResult);
        scanWindow.show(parent);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }
            decodeAlbumImage();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            PermissionUtils.requestCameraPermission(this);
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            PermissionUtils.requestCameraPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
        scanLine.startAnimation(scanAnimation);
        cameraResume();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void openWithBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(Intent.createChooser(intent, "请选择浏览器"));
        restartPreviewAfterDelay(1000);
    }

    /**
     * 进行中文乱码处理
     *
     * @param str
     * @return
     */
    private String recode(String str) {
        String formart = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
            } else {
                formart = str;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return formart;
    }
}