package com.performance.ubt.sdkTest.recorder;

/**
 * Created by brian.li on 2018/7/3.
 */

    public interface IAudioRecorder {
        void startRecord();
        void stopRecord();
        boolean isRecording();
        void setPcmDataListener(PcmDataListener pcmDataListener);
        void destroy();
    }
