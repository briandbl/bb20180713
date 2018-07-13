package com.iflytek.wakeup;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public interface IWakeupListener {
    void onWakeup(String resultStr, int soundAngle);

    void onError(int errCode);

    void onAudio(byte[] data, int dataLen, int param1, int param2);
}
