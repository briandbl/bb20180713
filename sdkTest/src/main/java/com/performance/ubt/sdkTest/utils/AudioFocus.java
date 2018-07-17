package com.performance.ubt.sdkTest.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by zz on 2016/10/26.
 */
public class AudioFocus implements AudioManager.OnAudioFocusChangeListener{
    private static final String TAG = "AudioFocus";

    private MediaPlayer mMediaPlayer;

    public AudioFocus(MediaPlayer mp){
        mMediaPlayer = mp;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.i(TAG, "focusChange:  "+focusChange);
        if(mMediaPlayer == null){
            return;
        }
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(0.01f, 0.01f);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(0.01f, 0.01f);
                }
                break;

        }
    }
}
