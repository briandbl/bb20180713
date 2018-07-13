package com.performance.ubt.sdkTest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.performance.ubt.sdkTest.R;
import com.ubtechinc.alpha.sdk.led.LedBright;
import com.ubtechinc.alpha.sdk.led.LedColor;
import com.ubtechinc.alpha.sdk.led.LedRobotApi;
import com.ubtechinc.alpha.serverlibutil.aidl.LedInfo;
import com.ubtechinc.alpha.serverlibutil.interfaces.LedListResultListener;
import com.ubtechinc.alpha.serverlibutil.interfaces.LedOperationResultListener;

import java.util.List;

public class LedFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "ledTest";
    private Spinner spinner;
    private static int colorIndex = 1;
    // only for turnOnEye function parameter set
    final String colors[] = new String[]{"null", "Red", "Green", "Blue", "Yellow", "品红", "青色", "white", "black"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView() {
        //initialize view
        initButton();
        LedRobotApi.get().initializ(mContext);

        //set ID object
        spinner = (Spinner) mView.findViewById(R.id.select_color);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, colors);
        //set Show Datas
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(1, true);

        //Register Event
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                colorIndex = position;
                Log.i(TAG, "colorIndex is " + colorIndex);
                TextView tv = (TextView) view;
                tv.setTextSize(16.0f);    //set font size
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                Toast.makeText(mContext, "selection" + spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext, "not change content", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_led;
    }

    @Override
    protected void getDataFromServer() {
    }

    /**
     * initialize button
     */
    private void initButton() {
        Button getLedListButton = (Button) mView.findViewById(R.id.getLedList);
        getLedListButton.setOnClickListener(this);
        Button turnOnEyeButton = (Button) mView.findViewById(R.id.turnOnEye);
        turnOnEyeButton.setOnClickListener(this);
        Button turnOffEyeButton = (Button) mView.findViewById(R.id.turnOffEye);
        turnOffEyeButton.setOnClickListener(this);
        Button turnOnEyeBlinkButton = (Button) mView.findViewById(R.id.turnOnEyeBlink);
        turnOnEyeBlinkButton.setOnClickListener(this);
        Button turnOnEyeFlashButton = (Button) mView.findViewById(R.id.turnOnEyeFlash);
        turnOnEyeFlashButton.setOnClickListener(this);
        Button turnOnEyeMarqueeButton = (Button) mView.findViewById(R.id.turnOnEyeMarquee);
        turnOnEyeMarqueeButton.setOnClickListener(this);
        Button turnOnHeadButton = (Button) mView.findViewById(R.id.turnOnHead);
        turnOnHeadButton.setOnClickListener(this);
        Button turnOffHeadButton = (Button) mView.findViewById(R.id.turnOffHead);
        turnOffHeadButton.setOnClickListener(this);
        Button turnOnHeadFlashButton = (Button) mView.findViewById(R.id.turnOnHeadFlash);
        turnOnHeadFlashButton.setOnClickListener(this);
        Button turnOnHeadMarqueeButton = (Button) mView.findViewById(R.id.turnOnHeadMarquee);
        turnOnHeadMarqueeButton.setOnClickListener(this);
        Button turnOnHeadBreathButton = (Button) mView.findViewById(R.id.turnOnHeadBreath);
        turnOnHeadBreathButton.setOnClickListener(this);
        Button turnOnMouthButton = (Button) mView.findViewById(R.id.turnOnMouth);
        turnOnMouthButton.setOnClickListener(this);
        Button turnOffMouthButton = (Button) mView.findViewById(R.id.turnOffMouth);
        turnOffMouthButton.setOnClickListener(this);
        Button turnOnMouthBreathButton = (Button) mView.findViewById(R.id.turnOnMouthBreath);
        turnOnMouthBreathButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getLedList:
                getLedList();
                break;
            case R.id.turnOnEyeBlink:
                turnOnEyeBlink();
                break;
            case R.id.turnOffEye:
                turnOffEye();
                break;
            case R.id.turnOnEyeFlash:
                turnOnEyeFlash();
                break;
            case R.id.turnOnEye:
                turnOnEye();
                break;
            case R.id.turnOnEyeMarquee:
                turnOnEyeMarquee();
                break;
            case R.id.turnOnHead:
                turnOnHead();
                break;
            case R.id.turnOnHeadFlash:
                turnOnHeadFlash();
                break;
            case R.id.turnOffHead:
                turnOffHead();
                break;
            case R.id.turnOnHeadMarquee:
                turnOnHeadMarquee();
                break;
            case R.id.turnOnHeadBreath:
                turnOnHeadBreath();
                break;
            case R.id.turnOnMouth:
                turnOnMouth();
                break;
            case R.id.turnOnMouthBreath:
                turnOnMouthBreath();
                break;
            case R.id.turnOffMouth:
                turnOffMouth();
                break;
            default:
                break;
        }

    }

    private void getLedList() {
        LedRobotApi.get().getLedList(new LedListResultListener() {
            @Override
            public void onGetLedList(int nOpId, int nErr, List<LedInfo> oArrLed) {
                Log.i(TAG, "getLedList return nOpId " + nOpId);
                for (LedInfo info : oArrLed) {
                    Log.i(TAG, "getLedId:" + info.getLedType());
                    for (int i = 0; i < info.getSupportColors().length; i++) {
                        Log.i(TAG, "getSupportColors:" + info.getSupportColors()[i]);
                    }
                    for (int i = 0; i < info.getSupportModes().length; i++) {
                        Log.i(TAG, "getSupportModes:" + info.getSupportModes()[i]);
                    }
                }
                Log.i(TAG, "getLedList return nErr " + nErr);
            }
        });
    }

    private void turnOnEyeBlink() {
        LedRobotApi.get().turnOnEyeBlink(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnEyeBlink nOpId " + nOpId);
                Log.i(TAG, "turnOnEyeBlink return " + nErr);
            }
        });
    }

    private void turnOffEye() {
        LedRobotApi.get().turnOffEyeLed(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffEyeBlink nOpId " + nOpId);
                Log.i(TAG, "turnOffEyeBlink return " + nErr);
            }
        });
    }

    private void turnOnEyeFlash() {
        LedRobotApi.get().turnOnEyeFlash(LedColor.valueOf(colorIndex), LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnEyeFlash nOpId " + nOpId);
                Log.i(TAG, "turnOnEyeFlash return " + nErr);
            }
        });
    }

    private void turnOnEye() {
        LedRobotApi.get().turnOnEyeLed(LedColor.GREEN, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffEyeFlash nOpId " + nOpId);
                Log.i(TAG, "turnOffEyeFlash return " + nErr);
            }
        });
    }

    private void turnOnEyeMarquee() {
        LedRobotApi.get().turnOnEyeMarquee(LedColor.valueOf(colorIndex), LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnEyeMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOnEyeMarquee return " + nErr);
            }
        });
    }

    private void turnOnHead() {
        LedRobotApi.get().turnOnHeadLed(LedColor.CYAN, LedBright.NINE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffEyeMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffEyeMarquee return " + nErr);
            }
        });
    }


    private void turnOnHeadFlash() {
        LedRobotApi.get().turnOnHeadFlash(LedColor.BLUE, LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnHeadFlash nOpId " + nOpId);
                Log.i(TAG, "turnOnHeadFlash return " + nErr);
            }
        });
    }

    private void turnOffHead() {
        LedRobotApi.get().turnOffHeadLed(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHead nOpId " + nOpId);
                Log.i(TAG, "turnOffHead return " + nErr);
            }
        });
    }

    private void turnOnHeadMarquee() {
        LedRobotApi.get().turnOnHeadMarquee(LedColor.BLUE, LedBright.FIVE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOnHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOnHeadMarquee return " + nErr);
            }
        });
    }

    private void turnOnHeadBreath() {
        LedRobotApi.get().turnOnHeadBreath(LedColor.BLUE, LedBright.NINE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffHeadMarquee nErr " + nErr);
            }
        });
    }

    public void turnOnMouth(){
        LedRobotApi.get().turnOnMouth(LedBright.NINE, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffHeadMarquee nErr " + nErr);
            }
        });
    }

    public void turnOnMouthBreath(){
        //Paramerter one ：Led turnOn Time；parameter two：Led turnOff Time；Parameter three：total time
        LedRobotApi.get().turnOnMouthBreath(1000, 500, 60000, new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffHeadMarquee nErr " + nErr);
            }
        });
    }

    public void turnOffMouth(){
        LedRobotApi.get().turnOffMouth(new LedOperationResultListener() {
            @Override
            public void onLedOpResult(int nOpId, int nErr) {
                Log.i(TAG, "turnOffHeadMarquee nOpId " + nOpId);
                Log.i(TAG, "turnOffHeadMarquee nErr " + nErr);
            }
        });
    }

}
