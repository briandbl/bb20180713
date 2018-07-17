package com.iflytek.wakeup;

import android.content.Context;
import android.util.Log;

import com.iflytek.cae.CAEEngine;
import com.iflytek.cae.CAEError;
import com.iflytek.cae.CAEListener;
import com.iflytek.cae.util.res.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class FlytekWakeup implements IWakeup{

    private static final String TAG = "FlytekWakeup";
    //WAKE UP workd "你好，小薇"
    public boolean isEn=false;
   public static final String CN_WAKEUP_NIHAO_XIAOWEI = "ivw/nihaoxiaowei.jet";
   // public static final String CN_WAKEUP_NIHAO_XIAOWEI = "ivw/alexa_three.jet";
    //public static final String CN_WAKEUP_NIHAO_XIAOWEI = "ivw/hello_alpha_en_three.jet";

    private CAEEngine mCAEEngine;
    private final String mResPath;
    private String DEFAULT_WAKEUP_WORD = CN_WAKEUP_NIHAO_XIAOWEI;
    public static int mEngineType = CAEEngine.SINGLE_WAKEUP; //CN
  // public static int mEngineType = CAEEngine.THREE_WAKEUP;//EN
    private int mWakeupThresholdMic5 = 25;
    private final Context mContext;
    private IWakeupListener mWakeupListener;
    private CAEListener mCaeListener ;
    public static volatile FlytekWakeup flytekWakeup;


    private FlytekWakeup(Context mContext){
        this.mContext = mContext;
        mResPath = ResourceUtil.generateResourcePath(mContext,ResourceUtil.RESOURCE_TYPE.assets, DEFAULT_WAKEUP_WORD);
        Log.i(TAG,"mResPath=" + mResPath);
        if(mCAEEngine == null)
            mCAEEngine = CAEEngine.createInstance(mResPath, mEngineType);
       // mCAEEngine.setShowCAELog(false);
        //CAE三唤醒词，set wakeup threshold...
            if (mEngineType == CAEEngine.THREE_WAKEUP) {
                mCAEEngine.setCAEWParam("ivw_threshold_1".getBytes(), String.valueOf(mWakeupThresholdMic5).getBytes());
                mCAEEngine.setCAEWParam("ivw_threshold_2".getBytes(), String.valueOf(mWakeupThresholdMic5).getBytes());
                mCAEEngine.setCAEWParam("ivw_threshold_3".getBytes(), String.valueOf(mWakeupThresholdMic5).getBytes());
//                mCAEEngine.setCAEWParam("wakeup_externel".getBytes(), String.valueOf(1).getBytes());
//                mCAEEngine.setCAEWParam("wakeup_enable".getBytes(), String.valueOf(0).getBytes());
            }
        Log.i(TAG,"mWakeupThresholdMic5=" + mWakeupThresholdMic5);
        if (null == mCAEEngine){
            Log.i(TAG,"CAEEngine create null mResPath = "+mResPath);
        }
    }

    public static FlytekWakeup getWakeup(Context context){
        if(flytekWakeup == null){
            synchronized (FlytekWakeup.class){
                if(flytekWakeup == null){
                    flytekWakeup =new FlytekWakeup(context);
                }
            }
        }
        return flytekWakeup;
    }


    @Override
    public void feedAudioData(byte[] pcmData, int dataLen) {
        mCAEEngine.writeAudio(pcmData,dataLen);
    }

    @Override
    public int extratData(byte[] inData, int inDataLen, int channel, byte[] outData) {
        return mCAEEngine.extract16K(inData, inDataLen, channel, outData);
    }

    @Override
    public void setWakeupListener(IWakeupListener listener) {
        this.mWakeupListener =listener;
        if(mCaeListener ==null ){
            mCaeListener = new CAEListener() {
                @Override
                public void onWakeup(String s) {
                    if(mWakeupListener !=null){
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            final String angle = json.getString("angle");
                            int soundAngle = Integer.parseInt(angle);
                            Log.d(TAG,"MicArray wakeup. result=" + soundAngle + " data " + s);
                            mWakeupListener.onWakeup(s, soundAngle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onAudio(byte[] bytes, int i, int i1, int i2) {
                    if(mWakeupListener !=null){
                        mWakeupListener.onAudio(bytes,i,i1,i2);
                    }
                }

                @Override
                public void onError(CAEError caeError) {
                    if(mWakeupListener !=null ){
                        mWakeupListener.onError(caeError.getErrorCode());
                    }
                }
            };
        }
        mCAEEngine.setCAEListener(mCaeListener);
    }

    @Override
    public void reset() {
        if(mCAEEngine !=null)
            mCAEEngine.reset();
    }

    @Override
    public void destroy() {
        if(mCAEEngine !=null)
            mCAEEngine.destroy();
        mCAEEngine = null;
        flytekWakeup =null;
    }

}
