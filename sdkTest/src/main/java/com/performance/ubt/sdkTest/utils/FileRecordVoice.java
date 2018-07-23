package com.performance.ubt.sdkTest.utils;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zz on 2016/10/11.
 */
public class FileRecordVoice {
    private static File mFile;
    private File mFile2;
    public static int nuanceSes = 0;

    public static void fileInit(){
        mFile = new File(Environment.getExternalStorageDirectory()+"/testF.pcm");
        try {
            if(mFile.exists()) {
                mFile.delete();
            }
            mFile.createNewFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeAudioToFile(byte[] data){
        if(mFile == null){
            return;
        }
        DataOutputStream writer = null;
        try {
            writer = new DataOutputStream(new FileOutputStream(mFile, true));
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void fileInit(String fileName){
        mFile2 = new File(Environment.getExternalStorageDirectory()+"/"+fileName);
        try {
            if(mFile2.exists()) {
                mFile2.delete();
            }
            mFile2.createNewFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeAudioToFile2(byte[] data){
        if(mFile2 == null){
            return;
        }
        DataOutputStream writer = null;
        try {
            writer = new DataOutputStream(new FileOutputStream(mFile2, true));
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeBytebuffToFile2(ByteBuffer data){
        if(mFile2 == null){
            return;
        }
        DataOutputStream writer = null;
        try {
            FileOutputStream fos = new FileOutputStream(mFile2, true);
            FileChannel fout = fos.getChannel();
            data.flip();
            fout.write(data);
            data.clear();
//            writer = new DataOutputStream();
//            writer.write(data);
//            writer.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
