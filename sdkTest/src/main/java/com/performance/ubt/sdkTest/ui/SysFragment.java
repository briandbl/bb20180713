package com.performance.ubt.sdkTest.ui;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.performance.ubt.sdkTest.R;
import com.ubtechinc.alpha.sdk.sys.SysApi;
import com.ubtechinc.alpha.serverlibutil.aidl.AlarmInfo;

public class SysFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "SysTest";

    @Override
    protected void initView() {
        //初始化view
        initButton();
        SysApi.get().initializ(mContext);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys;
    }

    @Override
    protected void getDataFromServer() {
    }

    /**
     * 初始化button
     */
    private void initButton() {
        Button getSidButton = (Button) mView.findViewById(R.id.getSid);
        getSidButton.setOnClickListener(this);
        Button getMICVersionButton = (Button) mView.findViewById(R.id.getMICVersion);
        getMICVersionButton.setOnClickListener(this);
        Button getChestVersionButton = (Button) mView.findViewById(R.id.getChestVersion);
        getChestVersionButton.setOnClickListener(this);
        Button getHeadVersionButton = (Button) mView.findViewById(R.id.getHeadVersion);
        getHeadVersionButton.setOnClickListener(this);
        Button isPowerChargingButton = (Button) mView.findViewById(R.id.isPowerCharging);
        isPowerChargingButton.setOnClickListener(this);
        Button getPowerValueButton = (Button) mView.findViewById(R.id.getPowerValue);
        getPowerValueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getSid:
                getSid();
                break;
            case R.id.getMICVersion:
                getMICVersion();
                break;
            case R.id.getChestVersion:
                getChestVersion();
                break;
            case R.id.getHeadVersion:
                getHeadVersion();
                break;
            case R.id.isPowerCharging:
                isPowerCharging();
                break;
            case R.id.getPowerValue:
                getPowerValue();
                break;
            default:
                break;
        }

    }
/*
 * get the serial number
 */
    private void getSid() {
        String sid = SysApi.get().getSid();
        Log.i(TAG, "getSid return " + sid);
        showToast("Sid:" + sid);
    }

    private void getMICVersion() {
        String micVersion = SysApi.get().getMICVersion();
        Log.i(TAG, "getMICVersion return " + micVersion);
        showToast("MICVersion:" + micVersion);
    }

    private void getChestVersion() {
        String chestVersion = SysApi.get().getChestVersion();
        Log.i(TAG, "getChestVersion return " + chestVersion);
        showToast("getChestVersion:" + chestVersion);
    }

    private void getHeadVersion() {
        String headVersion = SysApi.get().getHeadVersion();
        Log.i(TAG, "getHeadVersion return " + headVersion);
        showToast("getHeadVersion:" + headVersion);
    }

    private void isPowerCharging() {
        boolean isPowerCharging = SysApi.get().isPowerCharging();
        Log.i(TAG, "isPowerCharging return " + isPowerCharging);
        showToast("isPowerCharging:" + isPowerCharging);
    }

    private void getPowerValue() {
        int powerValue = SysApi.get().getPowerValue();
        Log.i(TAG, "getPowerValue return " + powerValue);
        showToast("getPowerValue:" + powerValue);
    }


    private void showToast(String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
