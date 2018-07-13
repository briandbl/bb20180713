package com.iflytek.recorder;

import com.iflytek.alsa.AlsaRecorder;
import com.iflytek.alsa.jni.AlsaJni;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class AlsaAudioRecorder  implements IAudioRecorder{
    private static volatile AlsaAudioRecorder mAlsaAudioRecorder;
    private AlsaRecorder mAudioRecorder;
    private PcmDataListener mPcmDataListener;
    private AlsaRecorder.PcmListener alsaPcmListener ;

    public static AlsaAudioRecorder get(){
        if(mAlsaAudioRecorder == null){
            synchronized (AlsaAudioRecorder.class){
                if(mAlsaAudioRecorder == null){
                    mAlsaAudioRecorder =new AlsaAudioRecorder();
                }
            }
        }
        return mAlsaAudioRecorder;
    }

    private AlsaAudioRecorder(){
        mAudioRecorder = AlsaRecorder.createInstance(0);
        AlsaJni.showJniLog(false);
    }
    @Override
    public void startRecord() {
        if(alsaPcmListener == null){
            alsaPcmListener = new AlsaRecorder.PcmListener() {
                @Override
                public void onPcmData(byte[] bytes, int i) {
                    if(mPcmDataListener !=null){
                        mPcmDataListener.onPcmData(bytes,i);
                    }
                }
            };
        }
        if(mAudioRecorder !=null && mPcmDataListener !=null && !mAudioRecorder.isRecording()){
            mAudioRecorder.startRecording(alsaPcmListener);
        }
    }

    @Override
    public void stopRecord() {
        if(mAudioRecorder !=null && mAudioRecorder.isRecording()){
            mAudioRecorder.stopRecording();
        }
    }

    public void destroy(){
        if(mAudioRecorder != null)
            mAudioRecorder.destroy();
        mAudioRecorder =null;
        mAlsaAudioRecorder =null;
    }

    @Override
    public boolean isRecording() {
        if(mAudioRecorder !=null){
            return mAudioRecorder.isRecording();
        }
        return false;
    }

    @Override
    public void setPcmDataListener(PcmDataListener pcmDataListener) {
        if(pcmDataListener !=null){
            this.mPcmDataListener =pcmDataListener;
        }
    }
}
