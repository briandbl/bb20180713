package com.ubtechinc.alpha.nuance;

import android.content.Context;

import com.iflytek.cae.jni.CAEJni;
import com.ubtech.utilcode.utils.LogUtils;
import com.ubtech.utilcode.utils.thread.ThreadPool;
import com.ubtechinc.alpha.ifytek.recorder.AlsaAudioRecorder;
import com.ubtechinc.alpha.nuance.recognizer.NuanceRecognizer;
import com.ubtechinc.alpha.nuance.ttser.NuanceTtser;
import com.ubtechinc.alpha.nuance.wakeuper.NuanceWakeuper;
import com.ubtechinc.alpha.speech.SpeechUtil;
import com.ubtechinc.alpha.speech.recognizer.IGrammarListener;
import com.ubtechinc.alpha.speech.recognizer.IRecognizerListener;
import com.ubtechinc.alpha.speech.recognizer.ISpeechRecognizer;
import com.ubtechinc.alpha.speech.recorder.IAudioRecorder;
import com.ubtechinc.alpha.speech.recorder.IPcmListener;
import com.ubtechinc.alpha.speech.recorder.RecorderProxy;
import com.ubtechinc.alpha.speech.ttser.ITTsListener;
import com.ubtechinc.alpha.speech.ttser.ITtser;
import com.ubtechinc.alpha.speech.wakeuper.IWakeup;
import com.ubtechinc.alpha.speech.wakeuper.IWakeupListener;
import com.ubtechinc.alpha.speech.wakeuper.WakeuperProxy;


/**
 * @desc :
 * @author: Logic
 * @email : logic.peng@ubtech.com
 * @time : 2017/7/30
 * @modifier:
 * @modify_time:
 */

public class NuanceUtil  implements SpeechUtil  {
    private static final String TAG = "NuanceUtil";
    private Context mContext;
    private IWakeup iWakeuper;//唤醒模块
    private IAudioRecorder iRecorder;//录音模块
    private ITtser iTtser;//tts模块
    private ISpeechRecognizer iRecognizer;//语音模块

    private volatile boolean mWakeuperEnabled;

    private IRecognizerListener mRecognizerListener;
    private IGrammarListener mGrammarListener;
    private IPcmListener mPcmListener;
    private IWakeupListener mWakeuperListener;

    private CAEJni.OutValues outValues = new CAEJni.OutValues();
    private IPcmListener mInnerPcmListener = new IPcmListener() {
        @Override
        public void onPcmData(byte[] data, int length) {
            final byte[] newdata = new byte[data.length / 16];
            //麦克风整列输出的原始音频是：96000采样，8声道，32bit pcm裸数据--logic.peng
            //ivw result:{"angle":27,"channel":1,"power":1472618299392,"CMScore":65,"beam":0}
            CAEJni.CAEExtract16K(data, data.length, 1, newdata, outValues);
            if (mWakeuperEnabled)
                iWakeuper.feedAudioData(newdata,newdata.length);
            if (mPcmListener != null){
                ThreadPool.runOnNonUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mPcmListener.onPcmData(newdata, newdata.length);
                    }
                });
            }
        }
    };

    public NuanceUtil(Context context) {
        this.mContext = context;
        iRecorder =new RecorderProxy(AlsaAudioRecorder.get());
        iWakeuper =new WakeuperProxy( new NuanceWakeuper(context));
        iWakeuper.init();
    }

    public void init() {
        LogUtils.i( "初始化Nuance语音引擎");
        iTtser = new NuanceTtser(mContext);
        iRecognizer = new NuanceRecognizer(mContext);
        iWakeuper.setWakeupListener(new IWakeupListener() {
            @Override
            public void onWakeup(String resultStr, int soundAngle) {
                if (mWakeuperListener != null){
                    mWakeuperListener.onWakeup(resultStr, soundAngle);
                }
            }

            @Override
            public void onError(int errCode) {
                iRecorder.stopRecord();
                iRecorder.startRecord();
            }

            @Override
            public void onAudio(byte[] data, int dataLen, int param1, int param2) {
                if (iRecognizer != null && iRecognizer.isListening()){
                    iRecognizer.writeAudio(data,0, dataLen);
                }
            }
        });
        iRecorder.setPcmListener(mInnerPcmListener);
        iRecorder.startRecord();//默认开启录音
    }


    public void release() {
        LogUtils.i(TAG, "释放Nuance语音引擎资源");
        if (iRecorder != null) {
            iRecorder.destroy();
            iRecorder = null;
        }
        if (iWakeuper != null){
            iWakeuper.destroy();
        }
        if (iTtser != null) {
            iTtser.destroy();
            iTtser = null;
        }
        if (iRecognizer != null) {
            iRecognizer.cancel();
            iRecognizer.destroy();
            iRecognizer = null;
        }
    }


    public void buildGrammar(String grammarStr, IGrammarListener listener) {
        this.mGrammarListener = listener;
        iRecognizer.buildGrammar(grammarStr,mGrammarListener);
    }


    public void initParam() {
        if (iTtser != null) {
            iTtser.init(); //初始化默认发音人
        }
        if (iRecognizer != null){
            iRecognizer.init();
        }
    }


    public void startTts(String text, ITTsListener listener) {
        if (iTtser != null) {
            iTtser.startSpeaking(text, listener);
        }
    }


    public void stopTts() {
        if (iTtser != null) {
            iTtser.stopSpeaking();
        }
    }

    private void restartRecognize() {
        iRecognizer.stopListening();
        iRecognizer.cancel();
        iRecognizer.startListening(mRecognizerListener);
    }


    public void startRecognize(IRecognizerListener listener){
        LogUtils.i(TAG,"启动语音引擎...");
        this.mRecognizerListener = listener;
        restartRecognize();
    }


    public void stopRecognize() {
        LogUtils.i(TAG,"关闭语音引擎...");
        iRecognizer.cancel();
        iRecognizer.stopListening();
    }


    public void enterWakeUp(IWakeupListener listener) {
        LogUtils.i(TAG,"进入唤醒模式...");
        this.mWakeuperListener = listener;
        if (!iRecorder.isRecording()){
            iRecorder.startRecord();
        }
        this.mWakeuperEnabled = true;
    }


    public void exitWakeUp() {
        LogUtils.i(TAG,"退出唤醒模式...");
        this.mWakeuperListener = null;
        this.mWakeuperEnabled = false;
    }


    public void enterRecoding(IPcmListener listener) {
        LogUtils.i(TAG,"进入录音模式...");
        this.mPcmListener = listener;
        if (!iRecorder.isRecording()){
            iRecorder.startRecord();
        }
    }


    public void exitRecoding() {
        LogUtils.i(TAG,"退出录音模式...");
        this.mPcmListener = null;
        if (iRecorder.isRecording()){
            iRecorder.stopRecord();
        }
    }


    public void setVoiceName(String voiceName) {
        if (iTtser != null) iTtser.setTtsVoicer(voiceName);
    }


    public String getCurVoiceName() {
        return iTtser ==null? "": iTtser.getTtsVoicer();
    }


    public void setTtsSpeed(String speed) {
        if (iTtser != null)
            iTtser.setTtsSpeed(speed);
    }


    public String getTtsSpeed() {
        return iTtser == null? "": iTtser.getTtsSpeed();
    }


    public void setTtsVolume(String volume) {
        if (iTtser != null)
            iTtser.setTtsVolume(volume);
    }


    public String getTtsVolume() {
        return iTtser == null? "": iTtser.getTtsVolume();
    }
}
