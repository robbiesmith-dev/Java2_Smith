package com.example.week1.app;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Robert Smith
//Java 2 Project 1 Term 1406
//June 7 2014
//Get Movie Service Class - Grabs JSON Returned from web and stores it in a file created using the ReadWriteLocalFile class

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetMovieService extends IntentService {

    public static final String MESSENGER_KEY = "messenger";
    public static final String URL_STRING = "urlString";
    public boolean success;

    URL url;
    ReadWriteLocalFile m_File;


    public GetMovieService() {
        super("GetMovieService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Context context = this;
            // Only allow access through getInstance()

            Bundle extras = intent.getExtras();
            Messenger messenger = (Messenger)extras.get(MESSENGER_KEY);
            ReadWriteLocalFile m_File;
            try
            {
                url = new URL("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=10&country=us&apikey=qveke3ymq3sejcq9w85ts7mc");
            }
            catch (MalformedURLException e)
            {
                Log.e("Error", "Exception caught: ", e);
            }
            int responseCode = -1;
            String response = "";

            try{
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
                    byte[] contextByte = new byte[1024];
                    int byteRead = 0;
                    StringBuffer responseBuffer = new StringBuffer();
                    while ((byteRead = input.read(contextByte))!= -1){
                        response = new String(contextByte, 0, byteRead);
                        responseBuffer.append(response);
                    }
                    response = responseBuffer.toString();
                    Log.e("TAG", "Response: " + response);

                }
                else
                {
                    Log.i("Error", "Error Code " + responseCode);
                }
            }
            catch (MalformedURLException e)
            {
                Log.e("Error", "Exception caught: ", e);
            }
            catch (IOException e) {
                Log.e("Error", "Exception caught: ", e);
            }
            m_File = ReadWriteLocalFile.getInstance();
            m_File.writeToFile(MainActivity.mContext, MainActivity.fileName, response);

            Message msg = Message.obtain();
            msg.arg1 = Activity.RESULT_OK;
            msg.obj = response;
            try
            {
                messenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }
}
