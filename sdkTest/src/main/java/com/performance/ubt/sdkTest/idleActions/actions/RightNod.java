package com.performance.ubt.sdkTest.idleActions.actions;


import com.performance.ubt.sdkTest.idleActions.IdleActionPlay;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/13 14:26
 * @描述:Right direction Nod
 */

public class RightNod extends IdleActionPlay {
    /**
     *   * first 20 data as action frame data ，last two data is action time
     */
    final byte[][] index = {
            {120, (byte) 203, 121, 120, 38, 115, (byte)120, 64, (byte)145, (byte)135, (byte)120, (byte)120, (byte)176, 95,  104, (byte)121, (byte)255, (byte)255, (byte)120, (byte)120, 10, 10},
            {120, (byte) 203, 121, 120, 38, 115, (byte)133, 58, (byte)126, (byte)149, (byte)130, (byte)132, (byte)172, 95,  98,  (byte)129, (byte)255, (byte)255, (byte)73,  (byte)148, 20, 10},
            {120, (byte) 203, 121, 120, 38, 115, (byte)110, 61, (byte)141, (byte)135, (byte)111, (byte)110, (byte)185, 107, 100, (byte)111, (byte)255, (byte)255, (byte)165, (byte)124, 20, 20},
            {120, (byte) 203, 121, 120, 38, 115, (byte)120, 64, (byte)145, (byte)135, (byte)120, (byte)120, (byte)176, 95,  104, (byte)121, (byte)255, (byte)255, (byte)120, (byte)120, 10, 10},
    };

    public RightNod(IIdlerCallBack callBack) {
        super(callBack);
        packetDatas = new PacketData[index.length];
        initPacket();
    }

    /**
     * init the data
     */
    private void initPacket() {
        for (int i = 0; i < index.length; i++) {
            packetDatas[i] = formatPacekt(index[i]);
        }
    }
}
