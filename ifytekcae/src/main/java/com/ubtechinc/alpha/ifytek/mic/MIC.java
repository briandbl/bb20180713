/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.nuance.mic;

import android.util.Log;

/**
 * @date 2016/8/23
 * @author paul.zhang@ubtrobot.com
 * @Description 导入讯飞lib库，提供算法识别mic
 * @modifier
 * @modify_time
 */

public class MIC {

	private static String TAG = "LOAD_MIC";
	//测试程序版本号
	private static String VERSION = "1.0.0.1001";

	public static int getChannel(int mic){
		int channel = 0;
		switch (mic){
			case 1:
				channel = 6;  //ADC1_4
				break;
			case 2:
				channel = 1;  //ADC1_1
				break;
			case 3:
				channel = 5;  //ADC1_2
				break;
			case 4:
				channel = 7;  //ADC1_2
				break;
			case 5:
				channel = 2;  //ADC1_3
				break;
			case 6:
				channel = 3;  //未使用
				break;
			case 7:
				channel = 4;  //REF1 参考信号
				break;
			case 8:
				channel = 8; //REF2 参考信号
				break;
		}
		return channel;
	}

	/**
	 * 获取测试程序版本号
	 * @return version
	 */
	public static String getVersion()
	{
		return VERSION;
	}

	/**
	 * 加载动态库
	 */
	static {
		try {
			System.loadLibrary("mictest");
		} catch (UnsatisfiedLinkError error) {
			Log.d(TAG, error.toString());
		}
	}

	/**
	 * 开启震动测试
	 * @param type ： 1
	 * @return
	 */
	public static native int[] vibrationTestWr(int[] data, int length);

	/**
	 * 开启录音测试
	 * @param type ： 1
	 * @return
	 */
	public static native int[] recordTestWr(int[] data, int length);
}
