package com.performance.ubt.sdkTest.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zz on 2016/10/26.
 */
public class MyMediaPlay implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{

    private static final String TAG = "MyMediaPlay";
    private static MyMediaPlay mInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean isAppend; //是否需要追加播放
    private AudioFocus mAudioFocus;
    private AudioManager mAudioManager;

    public MyMediaPlay(Context context) {
        mContext = context.getApplicationContext();
    }


    public MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null) {
            synchronized (MyMediaPlay.class) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setOnCompletionListener(this);
                    mMediaPlayer.setOnErrorListener(this);
                    mMediaPlayer.setOnPreparedListener(this);
                  //  mAudioFocus = new AudioFocus(mMediaPlayer);
                    mMediaPlayer.setVolume(1.0f, 1.0f);
                }
            }
        }
        return mMediaPlayer;
    }

    /**
     *  播放音乐
     * @param afd 音乐文件描述符
     * @param isAppend 播放完成后是否需要最加播放
     */
    public void play(AssetFileDescriptor afd, boolean isAppend){
        try {
            if(getMediaPlayer().isPlaying()){
                getMediaPlayer().stop();
            }

            getMediaPlayer().reset();
            mMediaPlayer.setVolume(1.0f, 1.0f);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            getMediaPlayer().prepareAsync();
            this.isAppend = isAppend;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否正在播放
     * @return
     */
    public boolean isPlaying(){
        return getMediaPlayer().isPlaying();
    }

    /**
     * 暂停播放
     */
    public void pause(){
        if(getMediaPlayer().isPlaying()) {
            getMediaPlayer().pause();
        }
    }

    /**
     *停止播放
     */
    public void stop(){
        if(getMediaPlayer().isPlaying()) {
            getMediaPlayer().stop();
        }
    }

    /**
     * 释放播放器
     */
    public void release(){
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG,"onCompletion isAppend="+isAppend);
        //Brian Solve the AUDIO STREAM COMPETETION PROBLEMS, CAUSE THE ALEXA APPLICATION DEAD LOCKED BEGINNING
//        if(mAudioManager != null){
//            Log.i(TAG,"RELEASE AUDIO FOCUS From Main Services ");
//            mAudioManager.abandonAudioFocus(mAudioFocus);
//        }
        //Brian Solve the AUDIO STREAM COMPETETION PROBLEMS, CAUSE THE ALEXA APPLICATION DEAD LOCKED ENDING
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i(TAG,"onError what:"+what+"    extra:"+extra);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG,"onPrepared");
//        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.requestAudioFocus(mAudioFocus, AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
        mMediaPlayer.start();
    }
}
