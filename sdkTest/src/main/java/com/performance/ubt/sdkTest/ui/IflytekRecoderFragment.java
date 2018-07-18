/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.performance.ubt.sdkTest.ui;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.iflytek.cae.CAEEngine;
import com.iflytek.cae.util.res.ResourceUtil;
import com.iflytek.mic.MIC;
import com.iflytek.mic.MicUtil;
import com.iflytek.recorder.AlsaAudioRecorder;
import com.iflytek.recorder.PcmDataListener;
import com.iflytek.wakeup.FlytekWakeup;
import com.iflytek.wakeup.IWakeupListener;
import com.performance.ubt.sdkTest.R;
import com.performance.ubt.sdkTest.app.ACContext;
import com.performance.ubt.sdkTest.app.DirType;
import com.performance.ubt.sdkTest.utils.AngleCheckUtils;
import com.performance.ubt.sdkTest.utils.MyMediaPlay;
import com.performance.ubt.sdkTest.utils.audio.AudioFileUtil;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedRobotApi;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.LedOperationResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorMoveAngleResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorReadAngleListener;
import com.ubtechinc.framework.util.thread.HandlerUtils;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.performance.ubt.sdkTest.utils.audio.AudioTrackUtil;
/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/10
 * @Description Iflytek recorder and wakeup test
 * @modifier
 * @modify_time
 */

public class IflytekRecoderFragment extends BaseFragement implements AudioTrackUtil.IPlayComplete {
	private static final String TAG = "FragmentRecoder";
	private static final String DIR="qrobot/audio/";
	private static final String PATH = Environment.getExternalStorageDirectory()+File.separator +DIR;;
	private static final String MIC_PATH = Environment.getExternalStorageDirectory()+File.separator +DIR;
	public static final String CN_WAKEUP_NIHAO_XIAOWEI = FlytekWakeup.CN_WAKEUP_NIHAO_XIAOWEI;
	private String DEFAULT_WAKEUP_WORD = CN_WAKEUP_NIHAO_XIAOWEI;
	private MyMediaPlay mTTSPlayer;


	private Button mBtnStart;
	private Button mBtnStop;
	private Button mPlayMusic;
	private Button mEmulateWakeup;
	private TextView mWakeupAngleText;
	private AudioTrackUtil mAudioTrackUtil;
	private AlsaAudioRecorder mRecorder;
	private CAEEngine mCaeEngine;
	private ExtractAudioThread mExtractThread;
	private String mResPath;

	private boolean isRecording=false;


	private int[] mBrokenMicList = new int[5];
	private Button mic1_play_btn;

	/** Android通道是否正在录音 **/
	private boolean isAndroidRecording = false;
	/** Android通道是否正在播放录音 **/
	private boolean isAndroidPlaying = false;
	FlytekWakeup mFlytekWakeup;
	private int curHeaderAngle = 0;
	private boolean isMusicPlaying=false;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initView() {

		LedRobotApi.get().initializ(mContext);
		mAudioTrackUtil = new AudioTrackUtil(getActivity(), MIC_PATH + "1.pcm");
		mAudioTrackUtil.setCompletListener(this);
		mRecorder = AlsaAudioRecorder.get();
		mRecorder.setPcmDataListener(mPcmListener);

		mFlytekWakeup=FlytekWakeup.getWakeup(mContext);
		mFlytekWakeup.setWakeupListener(new IWakeupListener() {
			@Override
			public void onWakeup(String resultStr, int soundAngle) {
				Log.d(TAG,"soundAngle" +soundAngle);
				mWakeupAngleText.setText("Angle："+soundAngle);
				handleAngle(soundAngle);

				HandlerUtils.runUITask(new Runnable() {
					@Override
					public void run() {
						LedRobotApi.get().turnOnHeadLed(LedColor.CYAN, LedBright.NINE, new LedOperationResultListener() {
							@Override
							public void onLedOpResult(int nOpId, int nErr) {
								Log.i(TAG, "turnOffEyeMarquee nOpId " + nOpId);
								Log.i(TAG, "turnOffEyeMarquee return " + nErr);
							}
						});
						try {
							Thread.sleep(600);
						}catch(Exception e){
							e.printStackTrace();
						}
						LedRobotApi.get().turnOffHeadLed(new LedOperationResultListener() {
							@Override
							public void onLedOpResult(int nOpId, int nErr) {
								Log.i(TAG, "turnOffHead nOpId " + nOpId);
								Log.i(TAG, "turnOffHead return " + nErr);
							}
						});
					}
				});
			}

			@Override
			public void onError(int errCode) {

			}

			@Override
			public void onAudio(byte[] data, int dataLen, int param1, int param2) {
				Log.d(TAG,"onAudio" +dataLen);
			}
		});

		mResPath = ResourceUtil.generateResourcePath(mContext,ResourceUtil.RESOURCE_TYPE.assets, DEFAULT_WAKEUP_WORD);
		Log.d(TAG,"mResPath "+mResPath);
		if(mCaeEngine == null)
			mCaeEngine = CAEEngine.createInstance(mResPath);

		Log.d(TAG,"mResPath  "+mResPath);

		Log.d(TAG,"record file path ---> " + PATH);
		mBtnStart = (Button) mView.findViewById(R.id.btn_recoder_start);
		mBtnStop = (Button) mView.findViewById(R.id.btn_recoder_stop);
		mic1_play_btn = (Button)mView.findViewById(R.id.mic1_play_btn);
		mPlayMusic=(Button)mView.findViewById(R.id.button);
		mEmulateWakeup=(Button)mView.findViewById(R.id.button2);
		mWakeupAngleText=(TextView)mView.findViewById(R.id.textView);


		mic1_play_btn.setTag(1);


		mBtnStart.setOnClickListener(this);
		mBtnStop.setOnClickListener(this);
		mic1_play_btn.setOnClickListener(this);
		mPlayMusic.setOnClickListener(this);
		mEmulateWakeup.setOnClickListener(this);
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_recoder;
	}

	@Override
	protected void getDataFromServer() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isAndroidRecording = false;
		isAndroidPlaying = false;
		releaseVoice();
		mAudioTrackUtil.release();
	}

	@Override
	protected void doClick(View v) {
		super.doClick(v);
		switch (v.getId()) {
			case R.id.btn_recoder_start:
				//record and stop
				recordAudio();
               break;
			case R.id.mic1_play_btn:
				playAudio(v);
				break;
			case R.id.button:
				if(!isMusicPlaying) {
					playMusic("audio/test.mp3");
					isMusicPlaying=true;
					mPlayMusic.setText("stop music");
				}else {
					stopMusic();
					isMusicPlaying=false;
					mPlayMusic.setText("play music");
				}
				break;
			case R.id.button2:
				mFlytekWakeup.externalWakeupEngineTriggerSetCaeParam();
				break;
		}
	}


	@Override
	public void onStop() {
		super.onStop();
	}


	private void recordAudio(){
		isAndroidRecording = false;
		isAndroidPlaying = false;
		mAudioTrackUtil.stop();
			ACContext.cleanPath(DirType.cache);
			if(!isRecording) {
				initRecordFile();
				showTip("recoder_start");
				mRecorder.startRecord();
				mBtnStart.setText("RECORDING");
				isRecording=true;
			}else {
				showTip("recoder_stop");
				releaseVoice();
				mBtnStart.setText("STOP");
				isRecording=false;
			}
	}

	private void playAudio(View v){
	{
		if (!mAudioTrackUtil.isPlaying()) {
			File tmp = new File(MIC_PATH + 1 + ".pcm");
			if (!tmp.exists()) {
				showTip("File not found " + 1+ "number mic file");
				return;
			}
			mAudioTrackUtil.stop();
			isAndroidPlaying = false;
			mAudioTrackUtil.setPath(MIC_PATH + 1 + ".pcm");
			mAudioTrackUtil.setDataSource(mAudioTrackUtil.initPCMData(MIC_PATH + 1+ ".pcm"));
			mAudioTrackUtil.start();
			showTip("PLAYING " + 1+ "MIC");

		}
		}
	}

	private int initRecordFile(){
		AudioFileUtil.initFile("1.pcm");
		return 0;
	}

	private void releaseVoice(){
		mRecorder.stopRecord();
	}

	@Override
	public void onPlayComplete(boolean success) {
		Log.d(TAG,"onPlayComplete ---> " + mAudioTrackUtil.getPath());
			HandlerUtils.runUITask(new Runnable() {
				@Override
				public void run() {
					isAndroidRecording = false;
					mRecorder.stopRecord();
					mAudioTrackUtil.stop();
					mBtnStart.setText("recoder_start");
				}
			});
	}

	PcmDataListener mPcmListener = new PcmDataListener() {
		@Override
		public void onPcmData(byte[] data, int length) {
			if (data ==  null){
				Log.d("paul","onPcmData empty");
				return;
			}
			mFlytekWakeup.feedAudioData(data,length);
			Log.d(TAG, "data:" + data);
			byte[] m16KAudio = new byte[4096];
			int outLen = mCaeEngine.extract16K(data, length, MIC.getChannel(1), m16KAudio); //mic --> channel
			Log.d(TAG, "check channel:" + 1 + "outlen is :" + outLen);
			AudioFileUtil.startRecordAudio(m16KAudio);
		}
	} ;


	/**
	 *
	 * @param data
	 * @param dataLen
     * @return 0 means success, the others represents broken mics number.
     */
	private int cheak5Mic(byte[] data, int dataLen) {
		//buffer size must large than output.len, Othersize return 0;
		byte[] m16KAudio = new byte[4096];
		int brokenMicNum = 0;
		for (int mic = 1; mic <= 12; mic ++) {
			int outLen = mCaeEngine.extract16K(data, dataLen, mic, m16KAudio); //mic --> channel
			if (outLen == 0) {
				mBrokenMicList[brokenMicNum] = mic;
				brokenMicNum ++;
			}
		}
		return brokenMicNum;
	}


	class ExtractAudioThread extends Thread {
		private boolean mStop = false;
		private ConcurrentLinkedQueue<byte[]> mAudioQueue = new ConcurrentLinkedQueue<byte[]>();

		// 存放得到的16K音频的缓冲区，大小为1024，这是由于每24576字节96K音频中可以抽取到1024字节的16K音频
		private byte[] m16KAudio = new byte[1024];

		public void stopRun() {
			mStop = true;
		}

		public void writeAudio(byte[] audio) {
			mAudioQueue.add(audio);
		}

		@Override
		public void run() {
			super.run();

			while (!mStop) {
				byte[] audio96K = mAudioQueue.poll();

				if (null != audio96K) {
					// extract16K, third parameter is about channel(7、1、8、2)
					int outLen = mCaeEngine.extract16K(audio96K, audio96K.length, 1, m16KAudio);

					Log.d("paul","extract16K channel:%d outLen:%d"+7+outLen);
				}
			}
			mAudioQueue.clear();
		}
	}

	private void handleAngle(int angle){
		if (curHeaderAngle == 0) {
			MotionRobotApi.get().readAbsoluteAngle(19, false, new MotorReadAngleListener() {
				@Override
				public void onReadMotorAngle(int nOpId, int nErr, int angle) {
					Log.i(TAG, "The Motion nOpId:" + nOpId);
					Log.i(TAG, "The Motion nRadian:" + angle);
					Log.i(TAG, "The Motion nErr:" + nErr);
					if(nErr==0) {
						curHeaderAngle =angle;
					}
				}
			});
		}
		final int curAngle = curHeaderAngle;
		LogUtils.I("19 servo : %d", curAngle);
		int detlaAngle = 0;
		if(angle > 270) { /** Robot Left **/
			detlaAngle = (byte)(angle - 360);
		} else if(angle < 90) {/** Robot Right **/
			detlaAngle = (byte)(angle);
		}
		LogUtils.I("19 servo delta：%d", detlaAngle);
		int newAngle = AngleCheckUtils.limitAngle(19, curAngle + detlaAngle);
		curHeaderAngle = newAngle;
		LogUtils.I("19 servo : %d", newAngle);
		if (newAngle > 10)
			MotionRobotApi.get().moveToAbsoluteAngle(19, newAngle, (short)500, new MotorMoveAngleResultListener() {
				@Override
				public void onMoveAngle(int nOpId, int nErr, int nRadian) {
					Log.i(TAG, "The Motion nOpId:" + nOpId);
					Log.i(TAG, "The Motion nRadian:" + nRadian);
					Log.i(TAG, "The Motion nErr:" + nErr);
				}
			});
	}


	private void playMusic(String path) {
		if (path == null) {
			return;
		}
		Log.d(TAG, "MUSIC PATH 12312             " + path);
		if(mTTSPlayer==null) {
			mTTSPlayer = new MyMediaPlay(mContext);
		}
		AssetFileDescriptor afd = null;
		try {
			afd = mContext.getAssets().openFd(path);
			if(!mTTSPlayer.isPlaying()){
				Log.d(TAG,"isPlaying stop ");
				mTTSPlayer.stop();
			}
			mTTSPlayer.play(afd, false);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(afd != null) {
					afd.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopMusic() {

		if(mTTSPlayer!=null) {
;           mTTSPlayer.stop();
		}
	}



}
