package com.performance.ubt.sdkTest.utils.audio;

import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class AudioFileUtil {
    private static final String DIR="qrobot/audio/";
    private static File audioFile;
    private static String mCurrentRecordPath;
    private static String getAudioFileName(){
        Date todayDate= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(todayDate);
    }

    public static void initFile(String preName){
        File audioFileDir= new File(Environment.getExternalStorageDirectory()+File.separator +DIR );
        if(audioFileDir.exists()){
            audioFileDir.delete();
        }
        if(!audioFileDir.exists()){
            audioFileDir.mkdirs();
        }
        audioFile =new File(audioFileDir, preName);
        long size =audioFile.length();
        if(size>0){
            Log.d("AudioFileUtil","SIZE IS "+size);
            audioFile.delete();
            audioFile =new File(audioFileDir, preName);
        }
        setRecordAudioFileName(preName);


        try {
            audioFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setRecordAudioFileName(String currentPath ){
        mCurrentRecordPath=currentPath;
    }
    public static String getRecordAudioFileName(){
        Log.d("AudioFIle","RECORD NAME "+mCurrentRecordPath);
        return mCurrentRecordPath;
    }

    public static void startRecordAudio(byte[] data){
        if(audioFile ==null){
            return ;
        }
        DataOutputStream writer =null;
        try {
            if(data !=null){
             //   writer =new DataOutputStream(new FileOutputStream(audioFile,true));
                writer =new DataOutputStream(new FileOutputStream(audioFile,true));
                writer.write(data);
                writer.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        } finally {
            if(writer !=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
