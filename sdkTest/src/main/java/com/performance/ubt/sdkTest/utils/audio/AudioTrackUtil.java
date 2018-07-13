/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.performance.ubt.sdkTest.utils.audio;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.performance.ubt.sdkTest.utils.IVoiceManager;
import com.performance.ubt.sdkTest.utils.thread.ThreadPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @date 2016/9/6
 * @author paul.zhang@ubtrobot.com
 * @Description 用于播放raw音乐文件
 * @modifier
 * @modify_time
 */

public class AudioTrackUtil implements IVoiceManager {

	private final String TAG = "AudioTrackUtil";
	private String mPath;
	private AudioParam mAudioParam;						// 音频参数
	private byte[] mData;								// 音频数据
	private AudioTrack mAudioTrack;						// AudioTrack对象
	private boolean mBReady = false;					// 播放源是否就绪
	private PlayAudioThread mPlayAudioThread;			// 播放线程
	private boolean mThreadExitFlag = false;			// 线程退出标志
	private int mPrimePlaySize = 0;						// 较优播放块大小
	private int mPlayOffset = 0;						// 当前播放位置
	private IPlayComplete mCompletListener;
	private boolean mStartPlaying = false;
	private AudioManager mAudioManager;
	private int currentVolume;


	public interface IPlayComplete{
		void onPlayComplete(boolean success);
	}


	public AudioTrackUtil(){

	}

	public AudioTrackUtil(Context context, String path) {
		this.mPath = path;
		this.mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		setDataSource(initPCMData(path));
		setAudioParam(initAudioParam());
	}

	public void setCompletListener(IPlayComplete mCompletListener) {
		this.mCompletListener = mCompletListener;
	}

	public void setPath(String Path){
		this.mPath = Path;
	}

	public String getPath(){
		return  mPath;
	}

	public void setAudioParam(AudioParam audioParam) {
		mAudioParam = audioParam;
	}

	public AudioParam initAudioParam()
	{
		AudioParam audioParam = new AudioParam();
		audioParam.mFrequency = 16000;
		audioParam.mChannel = AudioFormat.CHANNEL_OUT_MONO;
		audioParam.mSampBit = AudioFormat.ENCODING_PCM_16BIT;
		return audioParam;
	}

	public byte[] initPCMData(String filePath) {

		File file = new File(filePath);
		if (file == null){
			return null;
		}

		FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		byte[] data_pack = null;
		if (inStream != null){
			long size = file.length();

			data_pack = new byte[(int) size];
			try {
				inStream.read(data_pack);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		}

		return data_pack;
	}

	public void setDataSource(byte[] data)
	{
		mData = data;
	}

	public boolean prepare()
	{
		if (mData == null || mAudioParam == null) {
			return false;
		}
		if (mBReady){
			return true;
		}
		try {
			if(mAudioTrack == null){
				createAudioTrack();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		mBReady = true;
		return true;
	}

	public boolean play()
	{
		if (!mBReady) {
			return false;
		}
		mPlayOffset = 0;
		mStartPlaying = true;
		Log.d(TAG,"ready to play " + mPath);
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
		startThread();
		return true;
	}

	private void createAudioTrack() throws Exception
	{

		// 获得构建对象的最小缓冲区大小
		int minBufSize = AudioTrack.getMinBufferSize(mAudioParam.mFrequency,
				mAudioParam.mChannel,
				mAudioParam.mSampBit);


		mPrimePlaySize = minBufSize * 2;
		Log.d(TAG, "mPrimePlaySize = " + mPrimePlaySize);

		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				mAudioParam.mFrequency,
				mAudioParam.mChannel,
				mAudioParam.mSampBit,
				minBufSize,
				AudioTrack.MODE_STREAM);
	}

	private void startThread()
	{
		if (mPlayAudioThread == null)
		{
			mThreadExitFlag = false;
			mPlayAudioThread = new PlayAudioThread();
			mPlayAudioThread.start();
		}
	}

	private void stopThread()
	{
		if (mPlayAudioThread != null)
		{
			mThreadExitFlag = true;
			mPlayAudioThread = null;
		}
	}

	class PlayAudioThread extends Thread {


		@Override
		public void run() {

			Log.d(TAG, "PlayAudioThread run mPlayOffset = " + mPlayOffset);
			mAudioTrack.play();
			while(true)
			{
				if (mThreadExitFlag == true) {
					break;
				}
				try {
					int size = mAudioTrack.write(mData, mPlayOffset, mPrimePlaySize);
					mPlayOffset += mPrimePlaySize;

				} catch (Exception e) {
					e.printStackTrace();
					if (mCompletListener != null){
						mCompletListener.onPlayComplete(false);
						mStartPlaying = false;
						mThreadExitFlag=true;
					}
					break;
				}

				if(mData==null){
					return;
				}
				if (mPlayOffset >=mData.length) {
						mCompletListener.onPlayComplete(true);
						mThreadExitFlag=true;
						break;
				}
			}
			mAudioTrack.stop();
			Log.d(TAG, "PlayAudioThread complete...");
		}
	}

	@Override
	public boolean isPlaying() {
		return mStartPlaying;
	}

	@Override
	public void start() {
		prepare();
		play();
	}

	@Override
	public void stop() {
		if (!mBReady) {
			return ;
		}
		mStartPlaying = false;
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,AudioManager.FLAG_PLAY_SOUND);
		stopThread();
	}

	@Override
	public void release() {
		stop();
		if (mAudioTrack != null){
			mAudioTrack.stop();
			mAudioTrack.release();
			mAudioTrack = null;
		}
		mBReady = false;
		mStartPlaying = false;
	}

}
