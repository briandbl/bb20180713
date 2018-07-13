package com.performance.ubt.sdkTest.idleActions.actions;


import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/13 14:10
 * @描述:LEFT HAND TOUCH HEAD
 */

public class LeftTouchHead extends IdleActionPlay {
    /**
     * first 20 data as action frame data ，last two data is action time
     */
    final byte[][] index = {
            {119, (byte)210, 122, (byte)220, 20, 55,  120, 64, (byte)145, (byte)136, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, 92,  (byte)148, 20, 10},
            {119, (byte)210, 122, (byte)240, 24, 54,  120, 64, (byte)145, (byte)136, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, 79,  (byte)148, 10, 6},
            {119, (byte)210, 122, (byte)220, 20, 55,  120, 64, (byte)145, (byte)136, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, 95,  (byte)148, 10, 6},
            {119, (byte)210, 122, (byte)240, 24, 54,  120, 64, (byte)145, (byte)136, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, 79,  (byte)148, 20, 10},
            {119, (byte)203, 121, (byte)120, 34, 115, 120, 64, (byte)145, (byte)135, 120, 120, (byte)176, 95, 104, 121, (byte)255, (byte)255, 120, (byte)120, 20, 10},


    };

    public LeftTouchHead( IIdlerCallBack callBack) {
        super( callBack);
        packetDatas = new PacketData[index.length];
        initPacket();
    }


    /**
     *  init the data
     */
    private void initPacket() {
        for (int i = 0; i < index.length; i++) {
            packetDatas[i] = formatPacekt(index[i]);
        }
    }
}