package com.hohenheim.scancode.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hohenheim.R;
import com.hohenheim.common.activities.BaseActivity;
import com.hohenheim.common.adapter.PopWindowAdapter;
import com.hohenheim.common.permission.PermissionsController;
import com.hohenheim.common.utils.DateUtils;
import com.hohenheim.common.utils.JumpHelper;
import com.hohenheim.common.view.ScanPopWindow;
import com.hohenheim.scancode.camera.CameraManager;
import com.hohenheim.scancode.db.DBController;
import com.hohenheim.scancode.decode.DecodeHelper;
import com.hohenheim.scancode.decode.DecodeThread;
import com.hohenheim.scancode.utils.BeepManager;
import com.hohenheim.scancode.utils.CaptureActivityHandler;
import com.hohenheim.scancode.utils.InactivityTimer;
import com.google.zxing.Result;
import com.hohenheim.annotation.PermissionDenied;
import com.hohenheim.annotation.PermissionGrant;
import com.hohenheim.annotation.ShowRequestPermissionRationale;
import java.io.IOException;

public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final int SELECT_CODE = 1;//开启相册
    private static final int REQUEST_CAMERA = 2;//请求相机权限

    private boolean isHasSurface = false;
    private boolean isLight = false;//闪光灯是否开启
    private String scanResult;
    private String albumPath = null;
    private DBController controller = new DBController();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private TranslateAnimation scanAnimation;
    private ClipboardManager clipboard;

    private SurfaceView scanPreview;
    private ImageView scanLine;
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
    public void setContentView() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.scancode_activity_capture);
    }

    @Override
    public void findViews() {
        parent = (LinearLayout)findViewById(R.id.capture);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        findViewById(R.id.capture_crop_view).setOnClickListener(this);
    }

    @Override
    public void getData() {
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        //初始化动画
        scanAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.08f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        scanAnimation.setDuration(4500);
        scanAnimation.setRepeatCount(-1);
        scanAnimation.setRepeatMode(Animation.RESTART);

        if(scanWindow == null) {
            scanWindow = new ScanPopWindow(this);
            scanAdapter = new PopWindowAdapter(this, this);
            scanWindow.setAdapter(scanAdapter);
        }
    }

    @Override
    public void showContent() {

    }

    @Override
    protected void initToolBar(Toolbar toolBar) {
        super.initToolBar(toolBar);
        //返回
        findViewById(R.id.iv_title_back_button).setVisibility(View.VISIBLE);
        TextView leftContent = (TextView)findViewById(R.id.tv_title_back_text);
        leftContent.setVisibility(View.VISIBLE);
        leftContent.setText(getString(R.string.common_back));
        findViewById(R.id.btn_title_left).setOnClickListener(this);
        //标题栏
        ((TextView)findViewById(R.id.tv_title_text)).setText(getString(R.string.scan_code_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_history:
                startActivity(new Intent(CaptureActivity.this, HistoryActivity.class));
                break;
            case R.id.action_album:
                JumpHelper.openAlbum(this, SELECT_CODE, getString(R.string.scan_code_choose_album));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
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
                JumpHelper.openBrowser(this, scanResult, getString(R.string.scan_code_browser));
                restartPreviewAfterDelay(1000);
                break;
            case R.id.capture_crop_view:
                isLight = !isLight;
                cameraManager.setTorch(isLight);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
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
        scanLine.startAnimation(scanAnimation);
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

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
            scanResult = DecodeHelper.recode(rawResult.getText());
        } else {
            scanResult = DecodeHelper.recode(bundle.getString("result", ""));
        }
        if("".equals(scanResult)) {
            Toast toast = Toast.makeText(this, "无法识别", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        controller.saveHistory(DateUtils.getYearToDate(), DateUtils.getHourToSecond(), scanResult);
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
            requestPermission();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            requestPermission();
        }
    }

    private void requestPermission() {
        PermissionsController.requestPermissions(CaptureActivity.this, REQUEST_CAMERA, Manifest.permission.CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {
        PermissionsController.onRequestPermissionsResult(this, requestCode, permission, grantResult);
        super.onRequestPermissionsResult(requestCode, permission, grantResult);
    }

    @PermissionGrant(REQUEST_CAMERA)
    public void requestCameraSuccess() {
        Toast.makeText(this, "GRANT ACCESS Camera!", Toast.LENGTH_SHORT).show();
        cameraResume();
        scanLine.startAnimation(scanAnimation);
        PermissionsController.requestPermissions(CaptureActivity.this, REQUEST_CAMERA, Manifest.permission.CAMERA);
    }

    @PermissionDenied(REQUEST_CAMERA)
    public void requestCameraFailed() {
        Toast.makeText(this, "DENY ACCESS Camera!", Toast.LENGTH_SHORT).show();
    }

    @ShowRequestPermissionRationale(REQUEST_CAMERA)
    public void needCamera() {
        Toast.makeText(this, "I need flash!", Toast.LENGTH_SHORT).show();
        PermissionsController.requestPermissions(CaptureActivity.this, REQUEST_CAMERA, Manifest.permission.CAMERA);
    }

    /**
     * 重新开始扫描
     * @param delayMS 延迟时间
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }
}