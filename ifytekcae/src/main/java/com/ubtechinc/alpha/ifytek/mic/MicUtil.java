/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.nuance.mic;

import android.content.Context;
import android.util.Log;


import com.ubtechinc.alpha.nuance.utils.UbtSytem;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/23
 * @Description mic检测工具类目前讯飞提供的SDK只支持5mic检测、2mic算法暂时不支持
 * @modifier
 * @modify_time
 */

public class MicUtil {
	private static final String TAG = MicUtil.class.getSimpleName();
	public static final String RECORD_TEST_FILE = "recordtest.pcm";
	public static final String IFLYTEK_FILE = "iflytek_standard.pcm";
	private Context mContext;
	private IMicCallback mMicCallback;

	public MicUtil(Context context) {
		this.mContext = context;
	}

	public MicUtil(Context context, IMicCallback callback) {
		this.mContext = context;
		this.mMicCallback = callback;
	}

	public void setMicCallback(IMicCallback mMicCallback) {
		this.mMicCallback = mMicCallback;
	}

	public void recordTest() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					File file = new File("/sdcard/record.pcm");//ACContext.getDirectoryPath(DirType.cache)+ File.separator + MicUtil.RECORD_TEST_FILE
					FileInputStream fin = new FileInputStream(file);
					int length = fin.available();
					if(!file.exists()){
						LOGD("test file is null, please check ");
						if (mMicCallback != null){
							mMicCallback.onFail("测试文件不存在");
						}
						return;
					}
					if (length <= 0) {
						LOGD("test file is null, please check " + RECORD_TEST_FILE);
						if (mMicCallback != null){
							mMicCallback.onFail("测试文件不存在");
						}
					} else {
						byte[] bbuffer = new byte[length];
						fin.read(bbuffer);
						int[] ibuffer = bytesToInt(bbuffer, length);
						int ret[] = MIC.recordTestWr(ibuffer, ibuffer.length - 1280);
						int micCnt = UbtSytem.getMic();
						for (int i = 0; i < 13 ; i++) {
							if (ret[i] != 0) {
								LOGD("record test fail, reason is " + ret[i]);
							} else {
								LOGD("record test pass, reason is " + ret[i]);
							}
						}
						if (mMicCallback != null){
							mMicCallback.onSuccess(ret);
						}
					}
					fin.close();
				} catch (Exception e) {
					e.printStackTrace();
					LOGD("录音测试失败");
					if (mMicCallback != null){
						mMicCallback.onFail("录音测试失败");
					}
				}
			}
		}, 3 * 1000);
	}

	private int[] bytesToInt(byte[] src, int length) {
		int value = 0, offset = 0, i = 0;
		int[] ret = new int[length / 4 + 1];
		while (offset < length && length - offset >= 4) {
			value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8) | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
			offset += 4;
			ret[i] = value;
			i++;
		}
		return ret;
	}

	private static void LOGD(String str) {
		Log.d(TAG, str);
	}


	public static boolean getResult(int[] ret){
		int micCnt = UbtSytem.getMic();
		if (ret == null || micCnt > ret.length){
			return false;
		}
		for (int i = 0 ; i < micCnt;i++){
			if (ret[i] != 0){
				return false;
			}
		}
		return true;
	}

	public static String getErrorResult(int[] ret){
		int micCnt = UbtSytem.getMic();
		String err = " MIC : ";
		if (ret == null || micCnt > ret.length){
			return "";
		}
		for (int i = 0 ; i < micCnt;i++){
			if (ret[i] != 0){
				err += i;
				err += " ";
			}
		}
		return err;
	}
}
