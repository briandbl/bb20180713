package com.performance.ubt.sdkTest.idleActions;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.performance.ubt.sdkTest.idleActions.interfac.IActionForIdle;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.performance.ubt.sdkTest.utils.PacketData;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.serverlibutil.aidl.MotorAngle;
import com.ubtechinc.alpha.serverlibutil.interfaces.MotorSetAngleResultListener;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/12 15:19
 * @描述: action Execution
 */

public  class IdleActionPlay implements IActionForIdle {
    private static final String TAG = "AbsIdleAction";

    /**handler  msd id*/
    private static final int ACTION_START_PLAY = 1;
    private static final int ACTION_PLAY_END = 2;
    /**action default interval */
    protected final short ACTION_INTER = 50;  //50ms

    /**action datas package */
    protected PacketData[] packetDatas;
    protected int mPacketIndex = 0;


    /**handler thread */
    protected HandlerThread mHandlerThread;
    protected Handler mHandler;

    private short single_time = 900;

   public IdleActionPlay(Context mContext, final IIdlerCallBack callBack){
       MotionRobotApi.get().initializ(mContext);
        mHandlerThread = new HandlerThread("AbsIdleAction");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case ACTION_START_PLAY:
                        Log.d(TAG,"mPacketIndex="+mPacketIndex+"    packetDatas.length="+packetDatas.length);
                        if(mPacketIndex < packetDatas.length){
                           int value=0;
                            MotorAngle[] mas = new MotorAngle[20];
                            for(int i=0;i<20;i++){
                                mas[i]=new MotorAngle();
                                mas[i].setId(i+1);
                                if(packetDatas[mPacketIndex].getBuffer()[i]<0){
                                    value=256+packetDatas[mPacketIndex].getBuffer()[i];
                                }else {
                                    value=packetDatas[mPacketIndex].getBuffer()[i];
                                }
                                mas[i].setAngle(value);
                                Log.i(TAG, "The Motion angles :" +  value);
                            }

                            MotionRobotApi.get().setMotorAbsoluteAngles(mas, single_time, new MotorSetAngleResultListener() {
                                @Override
                                public void onSetMotorAngles(int nOpId, int nErr) {
                                    Log.i(TAG, "The Motion nOpId:" + nOpId);
                                    Log.i(TAG, "The Motion nErr:" + nErr);
                                }
                            });
//                            mSerialService.sendCommand(StaticValue.CHEST_SET_ALL_ANGLE,
//                                    packetDatas[mPacketIndex].getBuffer(), packetDatas[mPacketIndex].getBuffer().length);
                            mPacketIndex++;

                            byte[] dataMsg = packetDatas[mPacketIndex-1].getBuffer();
                            int len = dataMsg.length;
                            byte[] datas = {dataMsg[len-2],dataMsg[len-1]};
                            //Ready to next frame data
                            if(mPacketIndex < packetDatas.length) {
                                //get the previous frame time data
                                int time = getActionTime(datas);
                                mHandler.sendEmptyMessageDelayed(ACTION_START_PLAY, time);
                            }else{
                                Log.d(TAG,"action execution mPacketIndex="+mPacketIndex);
                                mHandler.sendEmptyMessageDelayed(ACTION_PLAY_END,
                                        getActionTime(datas));
                            }
                        }

                        break;

                    case ACTION_PLAY_END:
                        if(callBack != null){
                            callBack.onActionEndCallBack();
                        }
                        break;
                }
            }
        };
    }

    private int getActionTime(byte[] dataMsg){
        return PacketData.hBytesToShort(dataMsg);
    }
    /*s
     * Consstruct Action Data variable
     * @param dataMsg
     * @return package action datas
     */
   protected PacketData formatPacekt(byte[] dataMsg){
//    protected PacketData formatPacekt(int[] dataMsg) {
        PacketData packetData = new PacketData(2);
        for (int i = 0; i < dataMsg.length - 2; i++) {
//            packetData.putByte(dataMsg[i]);
            packetData.putByte(dataMsg[i]);
        }
        short time = (short) ((dataMsg[dataMsg.length - 2] + dataMsg[dataMsg.length - 1]) * ACTION_INTER);
        packetData.putShort_(time);
        return packetData;
    }

    @Override
    public void actionPlay() {
        Log.d(TAG,"actionPlay");
        if(!mHandler.hasMessages(ACTION_START_PLAY)){
            mPacketIndex = 0;
            mHandler.sendEmptyMessage(ACTION_START_PLAY);
            Log.d(TAG,"actionPlay mPacketIndex=0");
        }
    }

    @Override
    public void actionStop() {
        if(mHandler.hasMessages(ACTION_START_PLAY)){
            mPacketIndex = packetDatas.length;
            mHandler.removeMessages(ACTION_START_PLAY);
        }
        mHandler.removeMessages(ACTION_PLAY_END);
    }

}
