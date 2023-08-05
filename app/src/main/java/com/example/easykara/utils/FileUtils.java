package com.example.easykara.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static InputStream getFileInputStreamFromAsset(Context context, String fileName) {
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static File createFileFromInputStream(Context context,
                                                 InputStream inputStream,
                                                 String fileName) {

        try{
            File f = new File(context.getCacheDir()+ "/" + fileName);
            if (f.exists()) {
                return f;
            }
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }
}
