package com.ubtechinc.alpha.speech.wakeuper;

/**
 * @desc : 唤醒模块接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface IWakeup {
    void feedAudioData(byte[] pcmData, int dataLen);
    int extratData(byte[] inData, int inDataLen, int channel, byte[] outData);
    void setWakeupListener(IWakeupListener listener);
    void reset();
    void destroy();
    void init();
}
