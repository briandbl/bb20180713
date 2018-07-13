/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.performance.ubt.sdkTest.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/17
 * @Description Sensor Manager
 * @modifier
 * @modify_time
 */

public class SensorUtil implements SensorListener {

	private Context mContext;
	private SensorManager mSensorManager;
	private ISensorListener mSensorListener;

	public SensorUtil(Context context) {
		this.mContext = context;
		mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
	}

	public SensorUtil(Context context, ISensorListener listener) {
		this.mContext = context;
		this.mSensorListener = listener;
		mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
	}

	public void setSensorListener(ISensorListener listener) {
		this.mSensorListener = listener;
	}

	public void registerListener() {
		mSensorManager.registerListener(this, Sensor.TYPE_ACCELEROMETER | Sensor.TYPE_GYROSCOPE | Sensor.TYPE_LINEAR_ACCELERATION);
	}

	public void unregisterListener() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		if (mSensorListener != null) {
			mSensorListener.onSensorChanged(sensor, values[0], values[1], values[2]);
		}
	}

	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {

	}

	public static boolean isVaildSensor(float x, float y, float z){
		boolean ret = true;
		return ret;
	}

	public static boolean isVaildSonar(int distance){
		boolean ret = false;
		if (distance != 0){
			ret = true;
		}
		return ret;
	}
}
