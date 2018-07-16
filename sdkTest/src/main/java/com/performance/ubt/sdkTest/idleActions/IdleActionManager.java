package com.performance.ubt.sdkTest.idleActions;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.performance.ubt.sdkTest.idleActions.actions.LeftTouchHead;
import com.performance.ubt.sdkTest.idleActions.actions.RightNod;
import com.performance.ubt.sdkTest.idleActions.interfac.IActionForIdle;
import com.performance.ubt.sdkTest.idleActions.interfac.IIdlerCallBack;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;


import java.util.Random;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/9/12 17:30
 */

public class IdleActionManager implements IIdlerCallBack {
    private static final String TAG = "IdleActionManager";

    /**handler id*/
    private static final int ACTION_IDLE_PLAY = 1;

    private static final int INTER_TIME = 30000;//idle action interval
    private static final int START_TIME = 100;//idle action start

    public static IdleActionManager instance;
    private IActionForIdle[] actions;
    private Random random = new Random();
    private IActionForIdle currentAction;
    private Object actionLock = new Object();

    /**handler thread*/
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    /**flag whether is executing the action*/
    private boolean isPlaying = false;

    /**action intervals */
    private int[] actionInters = {10000, 15000, 20000, 25000, 30000};
    private Random mInterRandom = new Random();

    private IdleActionManager() {
        initActions();
        mHandlerThread = new HandlerThread("AbsIdleAction");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case ACTION_IDLE_PLAY:
                        randomActiomPlay();
                        break;
                }
            }
        };
    }

    public static IdleActionManager getInstance(Context mContext) {
        if (instance == null) {
            synchronized (IdleActionManager.class) {
                if (instance == null) {
                    MotionRobotApi.get().initializ(mContext);
                    instance = new IdleActionManager();
                }
            }
        }

        return instance;
    }

    /**
     * initialize the actions
     */
    private void initActions() {
        actions = new IActionForIdle[2];
        actions[0] = new LeftTouchHead(this);
        actions[1] = new RightNod(this);
    }


    /**
     * start action exectution
     */
    public void startPlay() {
        Log.i(TAG,"startPlay");
        synchronized (actionLock) {
            if(mHandler.hasMessages(ACTION_IDLE_PLAY)){
                mHandler.removeMessages(ACTION_IDLE_PLAY);
            }
            mHandler.sendEmptyMessageDelayed(ACTION_IDLE_PLAY, START_TIME);
        }
    }

    /**
     * stop action exectution
     */
    public void stopPlay() {
        Log.i(TAG,"stopPlay");
        synchronized (actionLock) {
            if(currentAction != null) {
                currentAction.actionStop();
                currentAction = null;
            }
            mHandler.removeMessages(ACTION_IDLE_PLAY);
            isPlaying = false;
        }
    }

    /**
     * get Random action file
     * @return
     */
    private IActionForIdle randomActiomPlay() {
        synchronized (actionLock) {
            int index = random.nextInt(actions.length);
            currentAction = actions[index];
            currentAction.actionPlay();
            isPlaying = true;
            Log.d(TAG,""+currentAction.getClass());
        }
        return currentAction;
    }


    @Override
    public void onActionEndCallBack() {
        Log.d(TAG,"onActionEndCallBack");
        synchronized (actionLock) {
            isPlaying = false;
            int index = mInterRandom.nextInt(actionInters.length);
            int time = actionInters[index];

            mHandler.sendEmptyMessageDelayed(ACTION_IDLE_PLAY, time);
        }
    }

    /**
     * action wheather is exeucting
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

}
