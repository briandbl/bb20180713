/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.performance.ubt.sdkTest.sensor;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/17
 * @Description G-Sensor callback
 * @modifier
 * @modify_time
 */

public interface ISensorListener {

	void onSensorChanged(int sensor, float x, float y, float z);

}
