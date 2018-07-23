package com.ubtechinc.alpha.ifytek.recorder;

import com.iflytek.alsa.AlsaRecorder;
import com.ubtechinc.alpha.speech.recorder.IAudioRecorder;
import com.ubtechinc.alpha.speech.recorder.IPcmListener;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class AlsaAudioRecorder  implements IAudioRecorder {
    private static volatile IAudioRecorder mAlsaAudioRecorder;
    private AlsaRecorder mAudioRecorder;
    private IPcmListener mPcmDataListener;
    private AlsaRecorder.PcmListener alsaPcmListener ;

    public static IAudioRecorder get(){
        if(mAlsaAudioRecorder == null){
            synchronized (AlsaAudioRecorder.class){
                if(mAlsaAudioRecorder == null){
                    mAlsaAudioRecorder =new AlsaAudioRecorder();
                }
            }
        }
        return mAlsaAudioRecorder;
    }

    public AlsaAudioRecorder(){
        mAudioRecorder = AlsaRecorder.createInstance(0);
       // AlsaJni.showJniLog(false);
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
    public void setBufferSize(int size) {

    }

    @Override
    public void setPcmListener(IPcmListener listener) {
        if(listener !=null){
            this.mPcmDataListener =listener;
        }
    }

    @Override
    public boolean isRecording() {
        if(mAudioRecorder !=null){
            return mAudioRecorder.isRecording();
        }
        return false;
    }

}
