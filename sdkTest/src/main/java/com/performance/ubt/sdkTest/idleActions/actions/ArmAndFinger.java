package com.performance.ubt.sdkTest.idleActions.actions;


import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/12 16:53
 * @描述: 手臂和手指动作
 */

public class ArmAndFinger extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {(byte)136, (byte)200, (byte)165, 110, 38, 78,  120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)120, (byte)120, 120, 120, 20, 20},
            {(byte)120, (byte)205, (byte)121, 120, 35, 115, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)120, (byte)120, 120, 120, 20, 20},
            {(byte)136, (byte)200, (byte)165, 110, 38, 78,  120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)120, (byte)120, 120, 120, 20, 20},
            {(byte)120, (byte)205, (byte)121, 120, 35, 115, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121,(byte)120, (byte)120, 120, 120, 20, 20},

    };

    public ArmAndFinger(Context mContext, IIdlerCallBack callBack) {
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
