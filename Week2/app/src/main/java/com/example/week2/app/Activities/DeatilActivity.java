package com.example.week2.app.Activities;

//Robert Smith
//Java 2 Project 2 Term 1406
//June 12 2014
//Detail Activity-  Displays information passed from mainactivity along with a rating bar and a link to rottentomatoes.com


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.week2.app.R;
import com.squareup.picasso.Picasso;


public class DeatilActivity extends Activity {

    public String title;
    public String rating;
    public String poster;
    public String link;
    ImageView posterIMG;
    TextView titleView, ratingView;
    Button rtButton;
    RatingBar ratingBar;
    float userRating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil);

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
        getMenuInflater().inflate(R.menu.deatil, menu);
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
}
