package com.example.week3.app.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.week3.app.Fragments.DetailFragment;
import com.example.week3.app.Fragments.MainFragment;
import com.example.week3.app.Helpers.GetMovieService;
import com.example.week3.app.Helpers.MovieData;
import com.example.week3.app.Helpers.MovieDataAdapter;
import com.example.week3.app.R;
import com.example.week3.app.Helpers.ReadWriteLocalFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity implements MainFragment.OnMovieSelectedListener {

    //Member Vars
    public static final String TAG = MainActivity.class.getSimpleName();
    public static Context mContext;
    private static ReadWriteLocalFile fileManager;
    public static String fileName = "JSON_String.txt";

    public static ArrayList<MovieData> MovieData_List;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landscape);
        mContext = this;

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

//    @Override
//    public void OnMovieSelected(String title, String rating, String poster, String link)
//    {
////        DetailFragment frag = (DetailFragment)getFragmentManager().findFragmentById(R.id.fragment_detail);
////        if(frag != null && frag.isInLayout())
////        {
////
//////            FragmentTransaction ft = getFragmentManager().beginTransaction();
//////            ft.replace(R.id.fragment_detail, new DetailFragment());
//////            ft.commit();
//////
//////            Bundle bundle = new Bundle();
//////            bundle.putString("title", title);
//////            bundle.putString("rating", rating);
//////            bundle.putString("poster", poster);
//////            bundle.putString("link", link);
//////
//////            frag.setArguments(bundle);
////            Intent detailIntent = new Intent(MainActivity.mContext, DetailActivity.class);
////            detailIntent.putExtra("title", title);
////            detailIntent.putExtra("rating", rating);
////            detailIntent.putExtra("poster", poster);
////            detailIntent.putExtra("link", link);
////            startActivityForResult(detailIntent, 0);
////        }
//    }

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
                    MainFragment.updateList();
                }
            }
        }

    }
}