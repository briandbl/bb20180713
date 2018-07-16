/*
 *
 *  * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *  *
 *
 */

package com.performance.ubt.sdkTest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.performance.ubt.sdkTest.R;
import com.performance.ubt.sdkTest.sensor.ISensorListener;
import com.performance.ubt.sdkTest.sensor.SensorUtil;
import com.ubtechinc.alpha.sdk.AlphaRobotApi;
import com.ubtechinc.alpha.sdk.SdkConstants;
import com.ubtechinc.alpha.sdk.listener.MuteKeyChangeListener;
import com.ubtechinc.alpha.sdk.listener.PirStateListener;
import com.ubtechinc.alpha.sdk.motion.MotionRobotApi;
import com.ubtechinc.alpha.serverlibutil.interfaces.ActionResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.PIRSensorOperationResultListener;


/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/10
 * @Description G-Sensor Test
 * @modifier
 * @modify_time
 */

public class AcceleratorFragment extends BaseFragement {

	//========================sensor ===============================
	private Button mBtnStart;
	private Button mBtnNext;
	private TextView mResult;
	private TextView mDataX;
	private TextView mDataY;
	private TextView mDataZ;
	private SensorUtil mSensorUtil;
	private boolean mRunning = false;
    private String TAG="Accelerometerer";
	private static final int UPTATE_INTERVAL_TIME = 480;
	private long lastUpdateTime;
	private float lastX;
	private float lastY;
	private float lastZ;
  //========================Key============================================

	public static final String HEADER_ACTION = "com.ubtechinc.services.header";
	public static final String ALPHA_FACTORY_KEY = "com.ubtechinc.key";
	public static final String MUTE_KKY="com.ubtechinc.services.Action.MUTE_KEY";
	public static final String MUTE_KEY_ACTION = "muteKeyAction";

	public static int KEY_VOLUME_UP = 4;
	public static int KEY_VOLUME_DOWN = 5;
	public static int KEY_HEAD_BREAK = 6;
	private static int KEY_VOLUME_UP_LYNX=10;
	private static int KEY_VOLUME_DOWN_LYNX=8;
	private ImageView mTouchUP;
	private ImageView mTouchDown;
	private ImageView mTouchMute;
	private int MUTE_PRESS=1;
	private int MUTE_RELEASE=2;
	private int MUTE_LONG_PRESS=3;
	PirStateListener mPirStateListener;
	private static Toast sToast;

	Button mPirEnable;
	Button mPirDisable;
	public TouchBroadcast mTouchBroadcast = new TouchBroadcast();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initView() {
		mSensorUtil = new SensorUtil(getActivity(),mListener);
		mSensorUtil.registerListener();
		mBtnStart = (Button) mView.findViewById(R.id.btn_start);
		mResult = (TextView)mView.findViewById(R.id.txt_result);
		mDataX = (TextView)mView.findViewById(R.id.data_x);
		mDataY = (TextView)mView.findViewById(R.id.data_y);
		mDataZ = (TextView)mView.findViewById(R.id.data_z);
		mBtnStart.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				mRunning = true;
			}
		});


		AlphaRobotApi.get().initializ(mContext);
		mTouchUP = (ImageView) mView.findViewById(R.id.iv_head_up);
		mTouchDown = (ImageView) mView.findViewById(R.id.iv_head_down);
		mTouchMute = (ImageView) mView.findViewById(R.id.iv_mute);


		mPirDisable=(Button)mView.findViewById(R.id.btn_pirDisable);
		mPirDisable.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
				sensorPirDisable();
			}
		});
		mPirEnable=(Button)mView.findViewById(R.id.btn_pirEnable);
		mPirEnable.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v)
			{
			 sensorPirEnable();
			}
		});

		//VOLUME DETECT
		IntentFilter filter = new IntentFilter();
		filter.addAction(HEADER_ACTION);
		filter.addAction(ALPHA_FACTORY_KEY);
		filter.addAction(MUTE_KKY);
		getActivity().registerReceiver(mTouchBroadcast, filter);


		AlphaRobotApi.get().registerMuteKeyChangeListener(new MuteKeyChangeListener() {
            @Override
            public void muteKeyChange(int action) {
                if(action == 1) {
				  showIndicateToast("key press");
                }if(action == 0) {
			      showIndicateToast("key release");
                }
            }
        });
		AlphaRobotApi.get().registerPirStateListener(new PirStateListener() {
			@Override
			public void onState(boolean b) {
				showIndicateToast("pir status "+b);
			}
		});

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_accelerator;
	}

	@Override
	protected void getDataFromServer() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSensorUtil.unregisterListener();
	}

  public void sensorPirEnable(){
	  AlphaRobotApi.get().setPIRSensor(true, new PIRSensorOperationResultListener() {
		  @Override
		  public void onPIRSensorOpResult(int i) {
			  showIndicateToast("enablePirSensor"+i);

		  }
	  });
  }
  public void sensorPirDisable(){
	  AlphaRobotApi.get().setPIRSensor(false, new PIRSensorOperationResultListener() {
		  @Override
		  public void onPIRSensorOpResult(int i) {
			  showIndicateToast("enablePirSensor"+i);

		  }
	  });

  }
	private ISensorListener mListener = new ISensorListener() {
		@Override
		public void onSensorChanged(int sensor, float x, float y, float z) {
			if (sensor != Sensor.TYPE_MAGNETIC_FIELD){
				return;
			}
			if (!mRunning){
				return;
			}
			mRunning = false;
			mDataX.setText(x+"");
			mDataY.setText(y+"");
			mDataZ.setText(z+"");
			rootLayDownOrientation(x,y,z);
		}
	};


	private void rootLayDownOrientation(float x,float y,float z){
		//currently detect time
		long currentUpdateTime = System.currentTimeMillis();
		//two times detect interval
		long timeInterval = currentUpdateTime - lastUpdateTime;
		//detect whether reach the detect interval
		if(timeInterval < UPTATE_INTERVAL_TIME)
			return;

		lastUpdateTime = currentUpdateTime;

		float coordX = x;
		float coordY = y;
		float coordZ = z;
		StringBuilder sb= new StringBuilder();
		sb.append("get Sensor Datas：");
		sb.append("\nX Direction ：");
		sb.append(x);
		sb.append("\nY Direction：");
		sb.append(y);
		sb.append("\nZ axis value ：");
		sb.append(z);
		mResult.setText(sb);
		//get x,y,z value
		float deltaX = coordX - lastX;
		float deltaY = coordY - lastY;
		float deltaZ = coordZ - lastZ;

		lastX = coordX;
		lastY = coordY;
		lastZ = coordZ;

		double speed = Math.sqrt(deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ)/timeInterval * 10000;

		if( coordY >= 9 && (coordZ> -2 && coordZ < 2 )  && (coordX> -3 && coordX < 3)){//Face up
			Log.d(TAG,"speed = " + speed + "timeInterval = " + timeInterval);
			Log.d(TAG,"Face up play action 1500201197001");
			mResult.setText("layDown Face up");
			mSensorUtil.unregisterListener();
			MotionRobotApi.get().playAction("front_climb_up", new ActionResultListener() {
				@Override
				public void onPlayActionResult(int nOpId, int nErr) {
					Log.i(TAG, "The Action OpId:" + nOpId);
					Log.i(TAG, "The Action nErr:" + nErr);
					mSensorUtil.registerListener();
				}
			});

		}else if ( coordY <= -9 && (coordZ> -2 && coordZ < 2) && (coordX> -3 && coordX < 3)) {//Face Down
			Log.d(TAG,"speed = " + speed + "timeInterval = " + timeInterval);
			Log.w(TAG,"Fac down play action 1500201197002");
			mResult.setText("layDown Face down");
			MotionRobotApi.get().playAction("back_climb_up", new ActionResultListener() {
				@Override
				public void onPlayActionResult(int nOpId, int nErr) {
					Log.i(TAG, "The Action OpId:" + nOpId);
					Log.i(TAG, "The Action nErr:" + nErr);
					mSensorUtil.registerListener();
				}
			});
		}
	}


	public class TouchBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			final String action = intent.getAction();
			if (action.equals(HEADER_ACTION)) {
				int keyValue = intent.getIntExtra("value", 6);
				Log.d("paul", "keyValue:" + keyValue + "ACTION " + action);
				if (keyValue == KEY_VOLUME_UP || keyValue == KEY_VOLUME_UP_LYNX) {
					mTouchUP.setImageResource(R.drawable.ic_touch_up_focus);
				} else if (keyValue == KEY_VOLUME_DOWN || keyValue == KEY_VOLUME_DOWN_LYNX) {
					mTouchDown.setImageResource(R.drawable.ic_touch_down_focus);
				} else if (keyValue == KEY_HEAD_BREAK) {
					//BREAK
					mTouchUP.setImageResource(R.drawable.ic_touch_up_focus);
					mTouchDown.setImageResource(R.drawable.ic_touch_down_focus);
				}
			} else if (action.equals(MUTE_KKY)) {
				int keyAction = intent.getIntExtra(MUTE_KEY_ACTION, 0);
				Log.d("paul", "MUTEValue:" + keyAction);
				if(keyAction==MUTE_PRESS){
					//PRESS
					mTouchMute.setImageResource(R.drawable.ic_dot_focus);
				}else if(keyAction==MUTE_RELEASE){
	               //RELEASE
					mTouchMute.setImageResource(R.drawable.ic_dot);
				}else if(keyAction==MUTE_LONG_PRESS){
					mTouchMute.setImageResource(R.drawable.ic_dot);
					showIndicateToast("mute long press");
				}

			}
		}
	}

	private void showIndicateToast(String text){
		sToast=Toast.makeText(mContext,text,Toast.LENGTH_LONG);
		sToast.show();
	}
}
