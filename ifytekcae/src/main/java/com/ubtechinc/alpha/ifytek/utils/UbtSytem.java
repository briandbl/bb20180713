/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.nuance.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UbtSytem {
	public static final int ALPHA2_HW_MIC_TYPE_2 = 2;
	public static final int ALPHA2_HW_MIC_TYPE_5 = 5;
	private static final String TAG = "UbtSytem";

	public static String getProperty(String key){
		Process process = null;
		String property = null;
		try {
			process = Runtime.getRuntime().exec("getprop "+key);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			BufferedReader input = new BufferedReader(ir);
			property = input.readLine().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return property;
	}

	public static String getSystemVersion() {
		String version = "";
		String property = UbtSytem.getProperty("ro.hardware.version");
		Log.d("paul", "property:" + property);
		if (!TextUtils.isEmpty(property) && property.equals("alpha2_10005")) {
			version = "alpha2_10005";
		} else if (!TextUtils.isEmpty(property) && property.equals("alpha2_10002")) {
			version = "alpha2_10002";
		} else {
			version = "alpha2_10002";
		}
		return version;
	}

	public static String getSystemBuildVersion() {
		String version = "";
		version = UbtSytem.getProperty("ro.build.version.incremental");
		return version;
	}
	public static int getMic() {
		int mic = ALPHA2_HW_MIC_TYPE_2;
		String property = UbtSytem.getProperty("ro.hardware.version");
		if (!TextUtils.isEmpty(property) && property.equals("alpha2_10005")) {
			mic = ALPHA2_HW_MIC_TYPE_5;
		}
		return mic;
	}

//	public static String getAudioPath(Context context) {
//		File f = new File(ACContext.getDirectoryPath(DirType.app) + "/m1.map");
//		if (!f.exists()) {
//			try {
//				InputStream is = context.getAssets().open("audio/audio_wait.mp3");
//				int size = is.available();
//				byte[] buffer = new byte[size];
//				is.read(buffer);
//				is.close();
//				FileOutputStream fos = new FileOutputStream(f);
//				fos.write(buffer);
//				fos.close();
//			} catch (Exception e) {
//			}
//		}
//		return f.getPath();
//	}

	/**
	 *  从assets目录中复制整个文件夹内容
	 *  @param  context  Context 使用CopyFiles类的Activity
	 *  @param  oldPath  String  原文件路径  如：/aa
	 *  @param  newPath  String  复制后路径  如：xx:/bb/cc
	 */
	public static void copyFilesFassets(Context context,String oldPath,String newPath) {
		try {
			String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
			if (fileNames.length > 0) {//如果是目录
				File file = new File(newPath);
				file.mkdirs();//如果文件夹不存在，则递归
				for (String fileName : fileNames) {
					copyFilesFassets(context,oldPath + "/" + fileName,newPath+"/"+fileName);
				}
			} else {//如果是文件
				InputStream is = context.getAssets().open(oldPath);
				FileOutputStream fos = new FileOutputStream(new File(newPath));
				byte[] buffer = new byte[1024];
				int byteCount=0;
				while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
					fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
				}
				fos.flush();//刷新缓冲区
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getAppVersion(Context context,String pkName) {
		try {
			String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
			return versionName;
		} catch (Exception e) {
		}
		return "";
	}
}