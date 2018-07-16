package com.performance.ubt.sdkTest.utils;

/**
 * Created by Administrator on 2017/3/16.
 */

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class CopyFolderFromAssets {
    private final static String TAG = "CopyFolderFromAssets";

    public static void copyFile(InputStream is, String targetFileFullPath) {
        Log.d("vvvvv",targetFileFullPath);


        String filename = targetFileFullPath;
        File dir = new File(targetFileFullPath);
        // 如果目录不中存在，创建这个目录
        try {
            if (!(new File(filename)).exists()) {
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFolderFromAssets(Context context, String rootDirFullPath, String targetDirFullPath) {
        Log.d(TAG, "CopyFolderFromAssets " + "rootDirFullPath-" + rootDirFullPath + " targetDirFullPath-" + targetDirFullPath);
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            for (String string : listFiles) {// 看起子目录是文件还是文件夹，这里只好用.做区分了
                Log.d(TAG, "name-" + rootDirFullPath + "/" + string);
                if (isFileByName(string)) {// 文件
                    copyFileFromAssets(context, rootDirFullPath + "/" + string, targetDirFullPath + "/" + string);
                } else {// 文件夹
                    String childRootDirFullPath = rootDirFullPath + "/" + string;
                    String childTargetDirFullPath = targetDirFullPath + "/" + string;

                        new File(childTargetDirFullPath).mkdirs();

                    copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "CopyFolderFromAssets " + "IOException-" + e.getMessage());
            Log.d(TAG, "CopyFolderFromAssets " + "IOException-" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static boolean isFileByName(String string) {
        if (string.contains(".")) {
            return true;
        }
        return false;
    }

    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d(TAG, "copyFileFromAssets ");
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d(TAG, "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
        }
    }
}

