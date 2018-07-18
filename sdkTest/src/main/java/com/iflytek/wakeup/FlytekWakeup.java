package com.iflytek.wakeup;

import android.content.Context;
import android.util.Log;


import com.iflytek.cae.CAEEngine;
import com.iflytek.cae.CAEError;
import com.iflytek.cae.CAEListener;
import com.iflytek.cae.jni.CAESessionInfo;
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
   //public static final String CN_WAKEUP_NIHAO_XIAOWEI = "ivw/nihaoxiaowei.jet";
   public static final String CN_WAKEUP_NIHAO_XIAOWEI = "ivw/alexa_three.jet";

    private CAEEngine mCAEEngine;
    private final String mResPath;
    private String DEFAULT_WAKEUP_WORD = CN_WAKEUP_NIHAO_XIAOWEI;
    private int mWakeupThresholdMic5 = 25;
    private final Context mContext;
    private IWakeupListener mWakeupListener;
    private CAEListener mCaeListener ;
    public static volatile FlytekWakeup flytekWakeup;
    public boolean  isThirdPartyWakeUpEngine=true;
    public static int mWakeUpAngle=-1;


    private FlytekWakeup(Context mContext){
        this.mContext = mContext;
        mResPath = ResourceUtil.generateResourcePath(mContext,ResourceUtil.RESOURCE_TYPE.assets, DEFAULT_WAKEUP_WORD);
        Log.i(TAG,"mResPath=" + mResPath);
        if(mCAEEngine == null)
            mCAEEngine = CAEEngine.createInstance(mResPath);
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
        if(isThirdPartyWakeUpEngine){
            initSetCaeMode();
        }
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

    /**
     *SET CAE MODE ,INITILIAZE NEED CALL THE FUNCTION
     */
    private void initSetCaeMode(){
        /**
         * set wakeup mode
         **/
        CAEEngine.getInstance().CAESetWParam( "wakeup_externel", "1");
        CAEEngine.getInstance().CAESetWParam( "wakeup_enable", "0");
    }

    /**
     * External wakeup engine trigger to call the function
     */
    public void externalWakeupEngineTriggerSetCaeParam(){
        CAEEngine.getInstance().CAESetWParam( "wakeup_flag", "1");
        getCaeAngelParam();
    }

    private void getCaeAngelParam(){
        CAESessionInfo sessionInfo = new CAESessionInfo();
        CAEEngine.getInstance().CAEGetWParam("angle_value", sessionInfo);
        Log.d(TAG, "Wakeup Angles ："+sessionInfo.buffer[0]);
        mWakeUpAngle=sessionInfo.buffer[0];
    }

}
