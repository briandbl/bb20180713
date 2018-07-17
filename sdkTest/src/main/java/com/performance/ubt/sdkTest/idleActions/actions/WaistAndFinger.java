package com.performance.ubt.sdkTest.idleActions.actions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/12 10:46
 * @描述: 摆腰及手指动作
 */

public class WaistAndFinger extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {120, (byte) 205, (byte) 121, 120, 35, 115, (byte) 120, 64, (byte) 145, (byte) 135, (byte) 120, (byte) 120, (byte) 176, 95, 104, (byte) 121, 119, 119, 120, 120, 20, 20},
            {126, (byte) 194, (byte) 151, 110, 43, 94,  (byte) 117, 64, (byte) 145, (byte) 135, (byte) 114, (byte) 116, (byte) 176, 95, 104, (byte) 115, 119, 119, 120, 119, 20, 30},
            {120, (byte) 205, (byte) 121, 120, 35, 115, (byte) 120, 64, (byte) 145, (byte) 135, (byte) 120, (byte) 120, (byte) 176, 95, 104, (byte) 121, 119, 119, 120, 120, 20, 20},
            {126, (byte) 194, (byte) 151, 110, 43, 94,  (byte) 130, 64, (byte) 145, (byte) 135, (byte) 129, (byte) 128, (byte) 176, 95, 104, (byte) 128, 119, 119, 120, 119, 20, 30},
            {126, (byte) 194, (byte) 151, 110, 43, 94,  (byte) 122, 55, (byte) 120, (byte) 151, (byte) 123, (byte) 122, (byte) 184, 113, 95, (byte) 121, 119, 119, 120, 119, 10, 30},
    };


    public WaistAndFinger(@NonNull Context mContext, IIdlerCallBack callBack) {
        super(mContext, callBack);
        packetDatas = new PacketData[index.length];
        initPacket();
    }

    /**
     * 初始化动作数据
     */
    private void initPacket() {
        for (int i = 0; i < index.length; i++) {
            packetDatas[i] = formatPacekt(index[i]);
        }
    }

}
