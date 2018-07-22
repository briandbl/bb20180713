/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.ubtechinc.alpha.nuance.mic;
/**
 * @date 2016/8/23
 * @author paul.zhang@ubtrobot.com
 * @Description mic检测结果回调
 * @modifier
 * @modify_time
 */

public interface IMicCallback {

	void onSuccess(int ret[]);

	void onFail(String reason);
}
