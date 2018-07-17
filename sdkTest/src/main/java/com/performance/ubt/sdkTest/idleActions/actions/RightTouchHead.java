package com.performance.ubt.sdkTest.idleActions.actions;

import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/13 14:20
 * @描述:右手摸头
 */

public class RightTouchHead extends IdleActionPlay {
    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {16,  (byte)210, (byte)184, 120, 33, 114, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, (byte)158, (byte)148, 20, 10},
            {0,   (byte)215, (byte)183, 120, 33, 114, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, (byte)169, (byte)147, 10, 6},
            {16,  (byte)210, (byte)184, 120, 33, 114, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, (byte)158, (byte)148, 10, 6},
            {0,   (byte)215, (byte)183, 120, 33, 114, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, (byte)169, (byte)147, 10, 6},
            {119, (byte)203, (byte)121, 120, 34, 115, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, (byte)120, (byte)120, 20, 20},


    };

    public RightTouchHead(Context mContext, IIdlerCallBack callBack) {
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
