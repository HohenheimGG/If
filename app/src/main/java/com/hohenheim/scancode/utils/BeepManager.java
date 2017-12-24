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

package com.hohenheim.scancode.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;


import com.hohenheim.R;

import java.io.Closeable;
import java.io.IOException;

/**
 * Manages beeps and vibrations for {@link com.hohenheim.scancode.activity.CaptureActivity}.
 */
public class BeepManager implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, Closeable {

	private static final String TAG = BeepManager.class.getSimpleName();

	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

    private boolean mPlayBeep;//是否铃声
    private boolean mVibrate;//是否震动
    private float mBeepVolume = BEEP_VOLUME;

	private Activity activity;
	private MediaPlayer mediaPlayer;

	public BeepManager(Activity activity) {
		this.activity = activity;
		initMedia();
	}

	//初始化
	private void initMedia() {
		mediaPlayer = null;
        getAudioSetting(activity);
        if (mPlayBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = buildMediaPlayer();
        }
	}

    //获取当前手机铃声状态
    private void getAudioSetting(Context context) {
        AudioManager audioService = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mBeepVolume = audioService.getStreamVolume(AudioManager.STREAM_RING);
        //获取当前手机铃声状态(震动/静音/铃声)
        switch(audioService.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                mPlayBeep = true;
                mVibrate = true;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                mPlayBeep = false;
                mVibrate = true;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                mPlayBeep = false;
                mVibrate = false;
                break;
        }
    }

    private MediaPlayer buildMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        try {
            AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();

            mediaPlayer.setVolume(mBeepVolume, mBeepVolume);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer.release();
            return null;
        }
    }

	public synchronized void playBeep() {
		if (mPlayBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (mVibrate) {
			Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// When the beep has finished playing, rewind to queue up another one.
		mp.seekTo(0);
	}

	@Override
	public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
		if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
			// Media server died, so put up an appropriate error toast if required
			// and finish
            if(activity != null && !activity.isFinishing())
			    activity.finish();
		} else {
			// possibly media player error, so release and recreate
			mp.release();
			initMedia();
		}
		return true;
	}

	@Override
	public synchronized void close() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	//activity destroy时执行
	public void shutdown() {
        close();
        activity = null;
    }

}
