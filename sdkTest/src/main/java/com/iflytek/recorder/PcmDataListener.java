package com.iflytek.recorder;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public interface PcmDataListener {
    void onPcmData(byte[] data, int length);
}
