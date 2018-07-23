package com.ubtechinc.alpha.speech.wakeuper;

/**
 * @desc : 唤醒模块代理类
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class WakeuperProxy implements IWakeup {
    private final IWakeup mWakeuperInterface;

    public WakeuperProxy(IWakeup mWakeuperInterface) {
        this.mWakeuperInterface = mWakeuperInterface;
    }

//    @Override
//    public void init() {
//        mWakeuperInterface.init();
//    }
//

    @Override
    public void feedAudioData(byte[] pcmData, int dataLen) {
        mWakeuperInterface.feedAudioData(pcmData,dataLen);
    }

    @Override
    public int extratData(byte[] inData, int inDataLen, int channel, byte[] outData) {
        mWakeuperInterface.extratData(inData,inDataLen,channel,outData);
        return outData.length;
    }

    @Override
    public void setWakeupListener(IWakeupListener listener) {
        mWakeuperInterface.setWakeupListener(listener);
    }

    @Override
    public void reset() {
        mWakeuperInterface.reset();
    }

    @Override
    public void destroy() {
        mWakeuperInterface.destroy();
    }

    @Override
    public void init() {

    }

}
