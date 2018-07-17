package com.performance.ubt.sdkTest.idleActions.actions;

import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/14 16:49
 * @描述:低头抬手
 */

public class NodLiftHands extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {120, (byte) 203, (byte) 121, (byte) 120, 38, 115, 120, 64, (byte) 145, (byte) 135, 120, 120, (byte) 176, 95, 104, 121, (byte)120, (byte)120, 120, (byte) 120, 10, 10},
            {101, (byte) 218, (byte) 180, (byte) 142, 23, 60,  120, 64, (byte) 145, (byte) 135, 120, 120, (byte) 176, 95, 104, 121, (byte)120, (byte)120, 110, (byte) 147, 20, 10},
            {80,  (byte) 154, (byte) 149, (byte) 166, 80, 88,  120, 64, (byte) 145, (byte) 135, 120, 120, (byte) 176, 95, 104, 121, (byte)120, (byte)120, 111, (byte) 99,  30, 20},
            {120, (byte) 203, (byte) 121, (byte) 120, 38, 115, 120, 64, (byte) 145, (byte) 135, 120, 120, (byte) 176, 95, 104, 121, (byte) 120, (byte) 120, 120, (byte) 120, 10, 10},

    };

    public NodLiftHands(Context mContext,IIdlerCallBack callBack) {
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
