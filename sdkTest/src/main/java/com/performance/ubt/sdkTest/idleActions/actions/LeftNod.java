package com.performance.ubt.sdkTest.idleActions.actions;


import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/13 14:31
 * @描述:左点头
 */

public class LeftNod extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {120, (byte) 203, 121, 120, 38, 115, (byte) 120, 64, (byte) 145, (byte) 135, (byte) 120, (byte) 120, (byte) 176, 95,  104, (byte) 121, (byte)120, (byte)120, (byte) 120, (byte) 120, 10, 10},
            {120, (byte) 203, 121, 120, 38, 115, (byte) 108, 60, (byte) 142, (byte) 133, (byte) 110, (byte) 108, (byte) 189, 113, 100, (byte) 111, (byte)120, (byte)120, (byte) 164, (byte) 148, 20, 10},
            {120, (byte) 203, 121, 120, 38, 115, (byte) 134, 55, (byte) 126, (byte) 145, (byte) 130, (byte) 134, (byte) 175, 96,  102, (byte) 131, (byte)120, (byte)120, (byte) 77,  (byte) 131, 20, 20},
            {120, (byte) 203, 121, 120, 38, 115, (byte) 120, 64, (byte) 145, (byte) 135, (byte) 120, (byte) 120, (byte) 176, 95,  104, (byte) 121, (byte)120, (byte)120, (byte) 120, (byte) 120, 10, 10},

    };

    public LeftNod(Context mContext,IIdlerCallBack callBack) {
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
