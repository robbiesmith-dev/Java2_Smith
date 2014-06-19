package com.example.week3.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    //Member Vars
    public static final String TAG = MainActivity.class.getSimpleName();
    public ListView mList;
    public TextView mListTypeText;
    public static Context mContext;
    private static ReadWriteLocalFile fileManager;
    public static String fileName = "JSON_String.txt";

    ArrayList<MovieData> MovieData_List;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiate Spinner
        mContext = this;

        //Instantiate Listview
        mList = getListView();

        //this nifty method automatically checks for position in the list view
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, "myPos " + i, Toast.LENGTH_LONG).show();

                //holder vars for MovieData properties
                String title, rating, poster, link;

                title = MovieData_List.get(i).movieName;
                rating = MovieData_List.get(i).rating;
                poster = MovieData_List.get(i).posterURL;
                link = MovieData_List.get(i).rtURL;

                //Intent that will pass properties to detail activity
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("title", title);
                detailIntent.putExtra("rating", rating);
                detailIntent.putExtra("poster", poster);
                detailIntent.putExtra("link", link);
                startActivityForResult(detailIntent, 0);

            }
        });

        mListTypeText = (TextView) findViewById(R.id.listTypeText);
        mListTypeText.setText(R.string.boxOfficeText);
        getJSONData();
    }

    //this grabs the intent from detail activity and constructs the alert view(if required)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == 0){
            if (data.hasExtra("userRating") && data.hasExtra("movie")){
                float ratingFromDetail = data.getExtras().getFloat("userRating");
                String movieFromDetail = data.getExtras().getString("movie");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Rating")
                        .setMessage("You gave " +movieFromDetail+ " a rating of "+ratingFromDetail)
                        .setCancelable(false)
                        .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
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
                MovieData_List = new ArrayList<MovieData>(10);
                //Log.e("TAG", "Movies: " + movies);

                String titlec,ratingc,posterc,linkc;
                for (int i = 0; i < movies.length(); i++)
                {
                    JSONObject movie = movies.getJSONObject(i);
                    titlec = movie.optString("title");
                    ratingc = movie.optString("mpaa_rating");
                    JSONObject posters = new JSONObject(movie.getString("posters"));
                    posterc = posters.optString("profile");
                    JSONObject rtLink  = new JSONObject(movie.getString("links"));
                    linkc = rtLink.optString("alternate");
                    MovieData film = new MovieData(titlec, ratingc, posterc, linkc);

                    Log.e("Array", "is" + titlec);

                    MovieData_List.add(film);
                    Log.e("Array 2", MovieData_List.get(i).getTitle());
                }
                Log.e("BAdpt", MovieData_List.get(4).getTitle());
                MovieDataAdapter adapter = new MovieDataAdapter(this, R.layout.list_item, MovieData_List);
                setListAdapter(adapter);
                //adapter.setContent(MovieData_List);
            }
            catch (JSONException e)
            {
                Log.e("TAG", "ERROR: " + e);
            }
        }
    }

    public static class HandleTheData extends Handler {

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