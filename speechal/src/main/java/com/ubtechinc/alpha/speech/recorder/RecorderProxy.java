package com.ubtechinc.alpha.speech.recorder;

/**
 * @desc : 录音代理
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public class RecorderProxy implements IAudioRecorder {
    private IAudioRecorder mRecoderInterface;

    public RecorderProxy(IAudioRecorder mRecoderInterface) {
        this.mRecoderInterface = mRecoderInterface;
    }


    @Override
    public void setPcmListener(IPcmListener listener) {
        mRecoderInterface.setPcmListener(listener);
    }

    @Override
    public void startRecord() {
         mRecoderInterface.startRecord();
    }

    @Override
    public boolean isRecording() {
        return mRecoderInterface.isRecording();
    }

    @Override
    public void stopRecord() {
        mRecoderInterface.stopRecord();
    }

    @Override
    public void destroy() {
        mRecoderInterface.destroy();
    }

    @Override
    public void setBufferSize(int size) {
        mRecoderInterface.setBufferSize(size);
    }
}
