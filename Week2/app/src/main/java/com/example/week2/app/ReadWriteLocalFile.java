package com.example.week2.app;

//Robert Smith
//Java 2 Project 1 Term 1406
//June 7 2014
//Read Write Local Files Class -  Creates a local file and allows the file to be read. Can only be accessed through getInstance

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public String readFile(Context context, String filename){

        String content = "";

        FileInputStream fls = null;

        try
        {
            fls = context.openFileInput(filename);
            BufferedInputStream bis = new BufferedInputStream(fls);
            byte[] contentBytes = new byte[1024];
            int bytesRead = 0;
            StringBuffer contentBuffer = new StringBuffer();

            while ((bytesRead = bis.read(contentBytes)) != -1)
            {
                content = new String(contentBytes, 0, bytesRead);
                contentBuffer.append(content);
            }
            content = contentBuffer.toString();
        }
        catch(Exception e)
        {

        }
        finally
        {
            try
            {
                fls.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        Log.e("FILE", "READS " + content);

        return content;
    }
}