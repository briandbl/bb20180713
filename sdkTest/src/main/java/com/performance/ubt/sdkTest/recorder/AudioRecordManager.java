package com.performance.ubt.sdkTest.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by brian.li on 2018/7/4.
 * Android standard audio recorder
 */

public class AudioRecordManager implements IAudioRecorder{
    public static final String TAG = "AudioRecordManager";
    private AudioRecord mRecorder;
    private DataOutputStream dos;
    private Thread recordThread;
    private boolean isStart = false;
    private static AudioRecordManager mInstance;
    private  int bufferSize;
    private PcmDataListener pcmDataListener;

    private final int SampleRateInHz = 16000;
    private final int ChannelConfig = AudioFormat.CHANNEL_IN_MONO;
    private final int AudioFormatBit = AudioFormat.ENCODING_PCM_16BIT;
    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };

    public AudioRecordManager() {
        bufferSize = AudioRecord.getMinBufferSize(SampleRateInHz, ChannelConfig, AudioFormatBit);
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SampleRateInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

    }


    /**
     * 获取单例引用
     *
     * @return
     */
    public static AudioRecordManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecordManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecordManager();
                }
            }
        }
        return mInstance;
    }
    /**
     * 销毁线程方法
     */
    private void destroyThread() {
        try {
            isStart = false;
            if (null != recordThread && Thread.State.RUNNABLE == recordThread.getState()) {
                try {
                    Thread.sleep(500);
                    recordThread.interrupt();
                } catch (Exception e) {
                    recordThread = null;
                }
            }
            recordThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordThread = null;
        }
    }

    /**
     * 启动录音线程
     */
    private void startThread() {
        destroyThread();
        isStart = true;
        if (recordThread == null) {
            recordThread = new Thread(recordRunnable);
            recordThread.start();
        }
    }

    /**
     * 录音线程
     */
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                int bytesRecord;
                //int bufferSize = 320;
                if (mRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
                    stopRecord();
                    return;
                }
                mRecorder.startRecording();
                byte buffer[]=null;
                buffer = new byte[bufferSize / 2];
                while (isStart) {
                    if (null != mRecorder) {
                        bytesRecord = mRecorder.read(buffer, 0, buffer.length);
                        if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (bytesRecord != 0 && bytesRecord != -1) {
                            pcmDataListener.onPcmData(buffer,buffer.length);
                        } else {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * 保存文件
     *
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        dos = new DataOutputStream(new FileOutputStream(file, true));
    }

    /**
     * start recording
     *
     */
    public void startRecord() {
        if(mRecorder==null){
            mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SampleRateInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        }
        try {
            startThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * stop recording
     */
    public void stopRecord() {
        try {

            if (mRecorder != null) {
                if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    mRecorder.stop();
                }
                if (mRecorder != null) {
                    mRecorder.release();
                }
                mRecorder=null;
            }
            if (dos != null) {
                dos.flush();
                dos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        destroyThread();
    }

    @Override
    public boolean isRecording() {
        return false;
    }

    @Override
    public void setPcmDataListener(PcmDataListener pcmDataListener) {
        this.pcmDataListener =pcmDataListener;
    }

    @Override
    public void destroy() {

    }

}