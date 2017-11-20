/*
 * Copyright (C) 2010 ZXing authors
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

package com.hohenheim.zxing.decode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.hohenheim.R;
import com.hohenheim.utils.ImageResize;
import com.hohenheim.zxing.activity.CaptureActivity;
import com.hohenheim.zxing.camera.CameraConfigurationManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();
	private final CaptureActivity activity;
	private final MultiFormatReader multiFormatReader;
	private boolean running = true;
    private static final Map<DecodeHintType, Object> decodeMap = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.AZTEC);
        list.add(BarcodeFormat.CODABAR);
        list.add(BarcodeFormat.CODE_39);
        list.add(BarcodeFormat.CODE_93);
        list.add(BarcodeFormat.CODE_128);
        list.add(BarcodeFormat.DATA_MATRIX);
        list.add(BarcodeFormat.EAN_8);
        list.add(BarcodeFormat.EAN_13);
        list.add(BarcodeFormat.ITF);
        list.add(BarcodeFormat.MAXICODE);
        list.add(BarcodeFormat.PDF_417);
        list.add(BarcodeFormat.QR_CODE);
        list.add(BarcodeFormat.RSS_14);
        list.add(BarcodeFormat.RSS_EXPANDED);
        list.add(BarcodeFormat.UPC_A);
        list.add(BarcodeFormat.UPC_E);
        list.add(BarcodeFormat.UPC_EAN_EXTENSION);
        decodeMap.put(DecodeHintType.POSSIBLE_FORMATS, list);
        decodeMap.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

	public DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		if (!running) {
			return;
		}
		switch(message.what) {
            case R.id.decode:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                break;
            case R.id.decode_path:
                Bundle bundle = message.getData();
                String path = bundle.getString("path", "");
                if(path.equals("")) {
                    decodeFailed(activity.getHandler());
                    return;
                }
                String content = decodeBytesToResult(path);
                Bundle result = new Bundle();

                result.putString("result", content);
                decodeSuccess(activity.getHandler(), result);
                break;
            case R.id.quit:
                running = false;
                Looper.myLooper().quit();
                break;
        }
	}

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * 
	 * @param data
	 *            The YUV preview frame.
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;
        // 判断相机拿的是横屏还是竖屏的数据
        if (CameraConfigurationManager.isCameraPortrait != CameraConfigurationManager.isScreenPortrait) {
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
            width ^= height;
            height ^= width;
            width ^= height;
            data = rotatedData;
        }
		PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(data, width, height);
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decodeWithState(bitmap);
			} catch (ReaderException re) {
				// continue
			} finally {
				multiFormatReader.reset();
			}
		}

		Handler handler = activity.getHandler();
		if (rawResult != null) {
			// Don't log the barcode contents for security.
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode in " + (end - start) + " ms");
			if (handler != null) {
				Message message = Message.obtain(handler, R.id.decode_succeeded, rawResult);
				Bundle bundle = new Bundle();
				bundleThumbnail(source, bundle);
				message.setData(bundle);
				message.sendToTarget();
			}
		} else {
            decodeFailed(handler);
		}
	}

	private void decodeSuccess(Handler handler, Bundle bundle) {
        if(handler == null)
            return;
        Message message = Message.obtain(handler, R.id.decode_succeeded);
        message.setData(bundle);
        message.sendToTarget();
    }

	private void decodeFailed(Handler handler) {
        if(handler == null)
            return;
        Message message = Message.obtain(handler, R.id.decode_failed);
        message.sendToTarget();
    }

    private String decodeBytesToResult(String path) {
        try {
            Bitmap bitmap = ImageResize.decodeZBitmapFromFile(new File(path));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] bytes = new int[width * height];
            bitmap.getPixels(bytes, 0, width, 0, 0, width, height);
            String result = getResult(bytes, width, height);
            if(!TextUtils.isEmpty(result)) {
                return result;
            } else {
                byte[] temp = new byte[width * height];
                bitmap.getPixels(bytes, 0, width, 0, 0, width, height);

                for(int index = 0; index < bytes.length; ++index) {
                    temp[index] = (byte)bytes[index];
                }

                return getResult(temp, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getResult(int[] bytes, int width, int height) {
        try {
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, bytes);
            Result result = (new MultiFormatReader()).decode(new BinaryBitmap(new HybridBinarizer(source)), decodeMap);
            return result.getText();
        } catch (Exception var5) {
            var5.printStackTrace();
            return "";
        }
    }

    private static String getResult(byte[] bytes, int width, int height) {
        try {
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(bytes, width, height, 0, 0, width, height, false);
            Result result = (new MultiFormatReader()).decode(new BinaryBitmap(new HybridBinarizer(source)), decodeMap);
            return result.getText();
        } catch (Exception var5) {
            var5.printStackTrace();
            return "";
        }
    }

	private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
		int[] pixels = source.renderThumbnail();
		int width = source.getThumbnailWidth();
		int height = source.getThumbnailHeight();
		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
	}

}
