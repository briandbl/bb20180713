package com.performance.ubt.sdkTest.idleActions.actions;


import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/13 14:50
 * @描述: 看右手
 */

public class SeeRightHand extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {120, (byte) 203, (byte) 121, 120, 38, 115, (byte) 120, 64, (byte) 145, (byte) 135, (byte) 120, (byte) 120, (byte) 176, 95, 104, 121, (byte) 255, (byte) 255, (byte) 120, (byte) 120, 10, 10},
            {116, (byte) 203, (byte) 180, 120, 37, 114, (byte) 139, 48, (byte) 110, (byte) 151, (byte) 129, (byte) 137, (byte) 175, 97, 102, 126, (byte) 255, (byte) 255, (byte) 145, (byte) 148, 30, 20},
            {120, (byte) 203, (byte) 121, 120, 38, 115, (byte) 120, 64, (byte) 145, (byte) 135, (byte) 120, (byte) 120, (byte) 176, 95, 104, 121, (byte) 255, (byte) 255, (byte) 120, (byte) 120, 10, 14},

    };

    public SeeRightHand(Context mContext,IIdlerCallBack callBack) {
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
