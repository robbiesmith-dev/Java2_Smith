package com.example.week1.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ListActivity {

    //Member Vars
    public static final String TAG = MainActivity.class.getSimpleName();
    public Spinner mSpinner;
    public ListView mList;
    public String mSpinnerText;
    public static String mData;
    public TextView mListTypeText;
    public URL url;
    public static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiate Spinner
        mContext = this;
        mSpinner = (Spinner) findViewById(R.id.spinner);

        //Instantiate Listview
        mList = (ListView) findViewById(R.id.list_item);

        mListTypeText = (TextView) findViewById(R.id.listTypeText);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Grab Text From Spinner
                mSpinnerText = mSpinner.getSelectedItem().toString();
                if (mSpinnerText.equals("Top Box Office")) {
                    try {
                        url = new URL("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?limit=10&country=us&apikey=qveke3ymq3sejcq9w85ts7mc");
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    mListTypeText.setText(R.string.boxOfficeText);
                } else {
                    try {
                        url = new URL("http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/top_rentals.json?limit=10&country=us&apikey=qveke3ymq3sejcq9w85ts7mc");
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    mListTypeText.setText(R.string.rentalText);
                }
                getData data = new getData();
                data.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;
    }


    public void updateList()
    {
        if(isNetworkAvailable())
        {
            if (mSpinnerText.equals("Top Box Office"))
            {
                JSON.getBoxOfficeJSON(mData);
                //Create Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, JSON.boxOfficeMovies);
                //Set Adapter
                setListAdapter(adapter);
            }
            else
            {
                JSON.getRentalJSON(mData);
                //Create Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, JSON.rentalMovies);
                //Set Adapter
                setListAdapter(adapter);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_LONG).show();
        }
    }

    //This will fetch the top 10 movies and saves them to a json object
    private class getData extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            int responseCode = -1;
            String response = "";
            JSONObject jsonResponse = null;
            try{
                //url from rottentomatoes.com
                URL dataURL = url;
                HttpURLConnection connection = (HttpURLConnection) dataURL.openConnection();
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
                    Log.i(TAG, "Error Code " + responseCode);
                }
            }
            catch (MalformedURLException e)
            {
                Log.e(TAG, "Exception caught: ", e);
            }
            catch (IOException e) {
                Log.e(TAG, "Exception caught: ", e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            mData = result;
            updateList();
            super.onPostExecute(result);
        }
    }

}
