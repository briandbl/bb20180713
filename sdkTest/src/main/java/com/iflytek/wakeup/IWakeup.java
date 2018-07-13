package com.iflytek.wakeup;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public interface IWakeup {
    void feedAudioData(byte[] pcmData, int dataLen);
     int extratData(byte[] inData, int inDataLen, int channel, byte[] outData);
    void setWakeupListener(IWakeupListener listener);
    void reset();
    void destroy();
}
