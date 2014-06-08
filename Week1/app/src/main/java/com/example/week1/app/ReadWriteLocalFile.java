package com.example.week1.app;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

/**
 * Created by robertsmith on 6/5/14.
 */
public class ReadWriteLocalFile {

    private static ReadWriteLocalFile m_instance;

    private ReadWriteLocalFile(){

    }

    public static ReadWriteLocalFile getInstance(){
        if(m_instance == null){
            m_instance = new ReadWriteLocalFile();
        }
        return m_instance;
    }

    public Boolean writeToFile(Context context, String filename, String content){
        Boolean result = false;

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(filename, context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("Write File Error", e.toString());
        }
        try {
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Write File Error", e.toString());
        }

        Log.i("Write file", "success");
        return result;
    }
}
