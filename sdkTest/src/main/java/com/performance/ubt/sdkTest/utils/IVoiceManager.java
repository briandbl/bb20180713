/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.performance.ubt.sdkTest.utils;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/15
 * @Description 录音\播放相关音频处理接口
 * @modifier
 * @modify_time
 */

public interface IVoiceManager {

	void start();

	void stop();

	void release();

	boolean isPlaying();
}
