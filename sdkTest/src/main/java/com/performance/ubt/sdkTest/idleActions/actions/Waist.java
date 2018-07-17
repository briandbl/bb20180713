package com.performance.ubt.sdkTest.idleActions.actions;


import android.content.Context;

import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/12 17:05
 * @描述: 扭腰肢动作
 */

public class Waist extends IdleActionPlay {

    /**
     * 前20个为动作，后两个数据为动作时间
     */
    final byte[][] index = {
            {125, (byte) 206, (byte) 139, 114, 36, 98, (byte) 117, 55, 118, (byte) 151, (byte) 114, (byte) 116, (byte) 187, 120, 93, (byte) 115, (byte)120, (byte)120, 120, 119, 20, 30},
            {126, (byte) 194, (byte) 151, 110, 43, 94, (byte) 130, 55, 120, (byte) 151, (byte) 129, (byte) 128, (byte) 184, 113, 93, (byte) 128, (byte) 119, (byte)119, 120, 119, 20, 30},
            {125, (byte) 206, (byte) 139, 114, 36, 98, (byte) 117, 55, 118, (byte) 151, (byte) 114, (byte) 116, (byte) 187, 120, 93, (byte) 115, (byte)120, (byte)120, 120, 119, 20, 30},
            {126, (byte) 194, (byte) 151, 110, 43, 94, (byte) 130, 55, 120, (byte) 151, (byte) 129, (byte) 128, (byte) 184, 113, 93, (byte) 128, (byte) 119, (byte) 119, 120, 119, 20, 30},
            {126, (byte) 194, (byte) 151, 110, 43, 94, (byte) 122, 55, 120, (byte) 151, (byte) 123, (byte) 122, (byte) 184, 113, 95, (byte) 121, (byte) 119, (byte) 119, 120, 119, 10, 30},

    };

    public Waist(Context mContext,IIdlerCallBack callBack) {
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
