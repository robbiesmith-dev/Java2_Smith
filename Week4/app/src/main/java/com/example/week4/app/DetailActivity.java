package com.example.week4.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailActivity extends Activity {

    public String title;
    public String rating;
    public String poster;
    public String link;
    ImageView posterIMG;
    TextView titleView, ratingView;
    Button rtButton;
    RatingBar ratingBar;
    float userRating;

    public static ArrayList<String> Favorites_List;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;


    static final String RATING = "rating";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            onRestoreInstanceState(savedInstanceState);
            userRating = savedInstanceState.getFloat(RATING);
        }
        //onRestoreInstanceState(savedInstanceState);

        setContentView(R.layout.activity_detail);

        titleView = (TextView)findViewById(R.id.titleView);
        ratingView = (TextView)findViewById(R.id.ratingView);
        posterIMG = (ImageView)findViewById(R.id.posterView);
        rtButton =(Button)findViewById(R.id.rtButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        Intent passedIntent = this.getIntent();
        title = passedIntent.getStringExtra("title");
        rating = passedIntent.getStringExtra("rating");
        poster = passedIntent.getStringExtra("poster");
        link = passedIntent.getStringExtra("link");

        rtButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(webIntent);
            }
        });

        titleView.setText(title);
        ratingView.setText(rating);
        Picasso.with(this).load(poster).into(posterIMG);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                userRating = ratingBar.getRating();

            }
        });

    }

    @Override
    public void finish(){

        Intent passedBackIntent = new Intent();
        passedBackIntent.putExtra("userRating", userRating);
        passedBackIntent.putExtra("movie", title);
        setResult(RESULT_OK, passedBackIntent);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putFloat(RATING, userRating);

        ratingBar.setRating(userRating);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
        edit = preferences.edit();
        if (preferences.getFloat("rating_" + title, ratingBar.getRating()) != 0.0f)
        {
            edit.putFloat("rating_" + title, ratingBar.getRating());
            edit.putString("title", title);
            edit.apply();
            if (Favorites_List != null)
            {
                if (!Favorites_List.contains(preferences.getString("title", "")))
                {
                    Favorites_List.add(preferences.getString("title", ""));
                    Log.e("DETAIL ACTIVITY", "" + Favorites_List);
                }
            }
            else
            {
                Favorites_List = new ArrayList<String>();

                Favorites_List.add(preferences.getString("title", ""));
                Log.e("DETAIL ACTIVITY", "FIRST INSTANCE" + Favorites_List);

            }
        }

   }

    @Override
    protected void onResume()
    {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
        ratingBar.setRating(preferences.getFloat("rating_" + title, ratingBar.getRating()));

    }

//    public void onRestoreInstanceState(Bundle savedInstanceState)
//    {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        userRating = savedInstanceState.getFloat(RATING);
//
//        ratingBar.setRating(userRating);
//    }
}