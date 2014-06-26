package com.example.week4.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

//Robert Smith
//Java 2 Project 2 Term 1406
//June 12 2014
//Main Activity Class - The First interface for the app. Displays a list of the top box office movies according to Rotten Tomatoes


public class MainActivity extends ListActivity {

    //Member Vars
    public static final String TAG = MainActivity.class.getSimpleName();
    public ListView mList;
    public TextView mListTypeText;
    public static Context mContext;
    private static ReadWriteLocalFile fileManager;
    public static String fileName = "JSON_String.txt";
    public static TextView username;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;

    //MADE ADAPTER MEMBER VAR SO I COULD CALL SEARCH METHOD FROM DIALOG METHOD
    public MovieDataAdapter adapter;

    ArrayList<MovieData> MovieData_List;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
        {
            Log.e("MAIN ACTIVITY", "SAVED INSTANCE STATE IS NOT NULL");

            MovieData_List = (ArrayList<MovieData>)savedInstanceState.getSerializable("movies");
            if (MovieData_List != null)
            {
                adapter = new MovieDataAdapter(this, R.layout.list_item, MovieData_List);
                setListAdapter(adapter);
            }
        }
        setContentView(R.layout.activity_main);
        //Instantiate Spinner
        mContext = this;
        username = (TextView)findViewById(R.id.username);


        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        username.setText(preferences.getString("username", ""));



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

    @Override
    protected void onSaveInstanceState(Bundle savedInstance)
    {
        super.onSaveInstanceState(savedInstance);

        if(MovieData_List != null && !MovieData_List.isEmpty())
        {
            savedInstance.putSerializable("movies", (Serializable)MovieData_List);
            Log.e("MAIN ACTIVITY", "SAVED INSTANCE IS STORED");
        }

    }

    //this grabs the intent from detail activity and constructs the alert view(if required)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == 0){
            if (data.hasExtra("userRating") && data.hasExtra("movie")){
                float ratingFromDetail = data.getExtras().getFloat("userRating");
                String movieFromDetail = data.getExtras().getString("movie");

                if (ratingFromDetail != 0.0f)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Rating")
                            .setMessage("You gave " + movieFromDetail + " a rating of " + ratingFromDetail)
                            .setCancelable(false)
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId())
        {
            case R.id.action_search:
                //SEARCH
                showDialog();
                break;
            case R.id.action_fav:
                showFavActivity();
                break;
            case R.id.action_userpref:
                //LOG USER IN
                showPrefDialog();
                break;
            case R.id.action_about:
                startInfoActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void startInfoActivity()
    {
        Intent infoIntent = new Intent(mContext, InfoActivity.class);
        startActivity(infoIntent);
    }

    void showFavActivity()
    {
        Intent favIntent = new Intent(mContext, FavoritesActivity.class);
        startActivity(favIntent);
    }

    void showDialog() {
        //CREATE INSTANCE OF DIALOG FRAG(ALLOWS USER TO FILTER LIST)
        DialogFragment newFragment = DialogFrag.newInstance(R.string.dialog);
        //SHOW
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void dialogOKClick()
    {
        //FILTERS LIST
        adapter.search(DialogFrag.input.getText().toString());
    }

    public void dialogResetClick()
    {
        //SET DIALOG EDIT TEXT TO ""
        DialogFrag.input.setText("");
        //CALL SEARCH METHOD WITH EMPTY STRING, WHICH WILL RETURN ALL THE MOVIES
        adapter.search(DialogFrag.input.getText().toString());
    }

    public void showPrefDialog()
    {
        //CREATE INSTANCE OF PREFDIALOGFRAG(THIS ASKS THE USER FOR USERNAME)
        PrefDialogFrag newFragment = PrefDialogFrag.newInstance(R.string.prefdialog);
        //SHOW
        newFragment.show(getFragmentManager(), "prefDialog");
    }

    public void prefDialogOKClick()
    {
        //CREATE INSTANCE OF PREFERENCE MANAGER
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //CREATE INSTANCE OF EDITOR CLASS
        edit = preferences.edit();
        //CHECK IF USERNAME VALUE EXIST
        if(preferences.getString("username", username.getText().toString()).isEmpty())
        {
            //POPULATE USERNAME
            edit.putString("username", PrefDialogFrag.input.getText().toString());
            //SET TEXT OF EDIT TEXT FROM PREFERENCES
            username.setText(preferences.getString("username", PrefDialogFrag.input.getText().toString()));
            //APPLY
            edit.apply();
        }
    }

    public void prefDialogCancelClick()
    {
        //THIS RESETS ALL SAVED PREFERENCES
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        edit = preferences.edit();
        edit.clear();
        edit.apply();
        DetailActivity.Favorites_List.clear();
        username.setText(preferences.getString("username", ""));
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        //FALSE BY DEFAULT
        boolean isAvailable = false;
        //CHECK IF TRUE
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
            //GET INSTANCE
            fileManager = ReadWriteLocalFile.getInstance();

            //READ CONTENTS OF FILE
            String response = fileManager.readFile(mContext, fileName);

            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray movies = jsonResponse.getJSONArray("movies");
                MovieData_List = new ArrayList<MovieData>(10);
                Log.e("TAG", "Movies: " + movies);

                String titlec,ratingc,posterc,linkc;
                for (int i = 0; i < movies.length(); i++)
                {
                    JSONObject movie = movies.getJSONObject(i);
                    titlec = movie.optString("title");
                    ratingc = movie.optString("mpaa_rating");
                    JSONObject posters = new JSONObject(movie.getString("posters"));
                    posterc = posters.optString("thumbnail");
                    JSONObject rtLink  = new JSONObject(movie.getString("links"));
                    linkc = rtLink.optString("alternate");
                    MovieData film = new MovieData(titlec, ratingc, posterc, linkc);

                    Log.e("Array", "is" + titlec);

                    MovieData_List.add(film);
                    Log.e("Array 2", MovieData_List.get(i).getTitle());
                }
                Log.e("BAdpt", MovieData_List.get(4).getTitle());
                adapter = new MovieDataAdapter(this, R.layout.list_item, MovieData_List);
                setListAdapter(adapter);
            }
            catch (JSONException e)
            {
                Log.e("TAG", "ERROR: " + e);
            }
        }
        else
        {
            //DISPLAYED IF NO INTERNET IS DETECTED
            Toast.makeText(mContext, "No Network Detected, Please Connect", Toast.LENGTH_LONG).show();
        }
    }

    //THIS IS RELATED TO THE SERVICE
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
