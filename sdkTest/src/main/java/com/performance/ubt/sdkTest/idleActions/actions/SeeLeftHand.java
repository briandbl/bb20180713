package com.performance.ubt.sdkTest.idleActions.actions;

import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/13 14:36
 * @描述:
 */

public class SeeLeftHand extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {120, (byte) 203, 121, (byte) 120, 38, 115, 120, 64, (byte) 145, (byte) 135, 120, 120, (byte) 176, 95, 104, 121, (byte)120, (byte)120, 120, (byte) 120, 10, 10},
            {119, (byte) 208, 121, (byte) 128, 34, 56,  102, 64, (byte) 152, (byte) 127, 109, 102, (byte) 195, 123, 95, 110, (byte)120, (byte)120, 63,  (byte) 148, 15, 10},
            {120, (byte) 203, 121, (byte) 120, 38, 115, 120, 64, (byte) 145, (byte) 135, 120, 120, (byte) 176, 95, 104, 121, (byte)120, (byte)120, 120, (byte) 120, 10, 14},

    };

    public SeeLeftHand(Context mContext, IIdlerCallBack callBack) {
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
