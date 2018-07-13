package com.iflytek.recorder;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public interface IAudioRecorder {
    void startRecord();
    void stopRecord();
    boolean isRecording();
    void setPcmDataListener(PcmDataListener pcmDataListener);
    void destroy();
}
