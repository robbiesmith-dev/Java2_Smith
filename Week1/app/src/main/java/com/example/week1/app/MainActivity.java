package com.example.week1.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.LogRecord;


//Robert Smith
//Java 2 Project 1 Term 1406
//June 7 2014
//Main Activity Class - The First and only interface for the app at this time. Displays a list of the top box office movies according to Rotten Tomatoes


public class MainActivity extends ListActivity {

    //Member Vars
    public static final String TAG = MainActivity.class.getSimpleName();
    public ListView mList;
    public TextView mListTypeText;
    public static Context mContext;
    private static ReadWriteLocalFile fileManager;
    public static String fileName = "JSON_String.txt";



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiate Spinner
        mContext = this;

        //Instantiate Listview
        mList = (ListView) findViewById(R.id.list_item);

        mListTypeText = (TextView) findViewById(R.id.listTypeText);
        mListTypeText.setText(R.string.boxOfficeText);
        getJSONData();
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

    public void getJSONData()
    {
        final HandleTheData myHandler = new HandleTheData(this);
        Messenger messenger = new Messenger(myHandler);
        Intent serviceIntent = new Intent(mContext, GetMovieService.class);
        serviceIntent.putExtra(GetMovieService.MESSENGER_KEY, messenger);
        startService(serviceIntent);
    }

    public void updateList()
    {
        if (isNetworkAvailable())
        {
            fileManager = ReadWriteLocalFile.getInstance();

            String response = fileManager.readFile(mContext, fileName);

            ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray movies = jsonResponse.getJSONArray("movies");
                //Log.e("TAG", "Movies: " + movies);

                for (int i = 0; i < movies.length(); i++)
                {
                    JSONObject movie = movies.getJSONObject(i);
                    String title = movie.getString("title");
                    String rating = movie.getString("mpaa_rating");

                    HashMap<String, String> displayMap = new HashMap<String, String>();
                    displayMap.put("title", title);
                    displayMap.put("rating", rating);

                    myList.add(displayMap);
                }
            }
            catch (JSONException e)
            {
                Log.e("TAG", "ERROR: " + e);
            }

            SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.list_item, new String[]{"title", "rating"}, new int[]{R.id.title, R.id.rating});
            //Set Adapter
            setListAdapter(adapter);

        }
    }

    public static class HandleTheData extends Handler{

        public HandleTheData(MainActivity activity)
        {
            weakActivity = new WeakReference<MainActivity>(activity);
        }

        private final WeakReference<MainActivity> weakActivity;

        @Override
        public void handleMessage(Message msg)
        {
            MainActivity activity = weakActivity.get();
            if(weakActivity != null)
            {
                Object object = msg.obj;
                if(msg.arg1 == RESULT_OK && object != null)
                {
                    activity.updateList();
                }
            }
        }

    }
}
