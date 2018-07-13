package com.performance.ubt.sdkTest.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.performance.ubt.sdkTest.R;
import com.performance.ubt.sdkTest.recorder.AudioRecordManager;
import com.performance.ubt.sdkTest.recorder.PcmDataListener;
import com.performance.ubt.sdkTest.utils.audio.AudioFileUtil;
import com.performance.ubt.sdkTest.utils.audio.AudioTrackUtil;

import java.io.File;
import android.os.Environment;

public class RecorderFragment extends BaseFragement implements View.OnClickListener {

    private static final String TAG = "SpeechTest";
    private Button btnStart, btnStop, btnPlay, btnFinish;
    private String fileName="recorder.pcm";
    private static final String DIR="qrobot/audio/";
    private String filePath=Environment.getExternalStorageDirectory()+File.separator +DIR;
    //PLAY RECORD PCM DATA TO CHECKT PURPOSE
    private AudioTrackUtil mAudioTrackUtil;
    private boolean isRecording=false;
    private boolean isPlaying=false;
    @Override
    protected void initView() {

        AudioRecordManager.getInstance().setPcmDataListener(mPcmDataListener);
        mAudioTrackUtil = new AudioTrackUtil(getActivity(),filePath);

        btnStart = (Button) mView.findViewById(R.id.btn_start);
        btnStart.setText("start");
        btnStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(!isRecording) {
                    AudioRecordManager.getInstance().startRecord();
                    AudioFileUtil.initFile(fileName);
                    Log.d(TAG, "recorder ");
                    btnStart.setText("RECORDING...");
                    isRecording=true;
                }else {
                    AudioRecordManager.getInstance().stopRecord();
                    btnStart.setText("STOP");
                    isRecording=false;
                }
            }
        });

        btnPlay = (Button) mView.findViewById(R.id.btn_play);
        btnPlay.setText("play");
        btnPlay.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                        if(!isPlaying) {
                            playAudioPcm(filePath + AudioFileUtil.getRecordAudioFileName());
                            btnPlay.setText("play audio");
                            isPlaying=true;
                        }else {
                            stopAudioPcm();
                            btnPlay.setText("play");
                            isPlaying=false;
                        }

            }
        });

    }

    private PcmDataListener mPcmDataListener=new PcmDataListener() {
        @Override
        public void onPcmData(byte[] data, int length) {
             AudioFileUtil.startRecordAudio(data);
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_recorder;
    }

    @Override
    protected void getDataFromServer() {
    }



    public void playAudioPcm(String absolutePath){
        if(mAudioTrackUtil!=null) {
            mAudioTrackUtil.stop();
            mAudioTrackUtil.setCompletListener(new AudioTrackUtil.IPlayComplete() {
                @Override
                public void onPlayComplete(boolean success) {
                    isPlaying=false;
                }
            });
            mAudioTrackUtil.setPath(absolutePath);
            mAudioTrackUtil.setDataSource(mAudioTrackUtil.initPCMData(absolutePath));
            mAudioTrackUtil.start();


        }
    }

    public void stopAudioPcm(){
        if(mAudioTrackUtil!=null)
        mAudioTrackUtil.stop();
    }

    @Override
    public void onClick(View view) {

    }
}
