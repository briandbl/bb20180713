package com.ubtechinc.alpha.speech.recorder;

/**
 * @desc : 录音模块接口
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/29
 * @modifier:
 * @modify_time:
 */

public interface IAudioRecorder {
    void setPcmListener(IPcmListener listener);

    void startRecord();

    boolean isRecording();

    void stopRecord();

    void destroy();

    void setBufferSize(int size);
}
