package com.example.week3.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.week3.app.Fragments.DetailFragment;
import com.example.week3.app.R;
import com.squareup.picasso.Picasso;


public class DetailActivity extends Activity{

    public String title;
    public String rating;
    public String poster;
    public String link;
    float userRating;
    public static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

//        rtButton = (Button)findViewById(R.id.rtButton);

//        rtButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v)
//            {
//                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                startActivity(webIntent);
//            }
//        });


        Intent passedIntent = this.getIntent();
        title = passedIntent.getStringExtra("title");
        rating = passedIntent.getStringExtra("rating");
        poster = passedIntent.getStringExtra("poster");
        link = passedIntent.getStringExtra("link");

//        Bundle bundle = new Bundle();
//        bundle.putString(title, "title");
//        bundle.putString(rating, "rating");
//        bundle.putString(poster, "poster");
//        bundle.putString(link, "link");
//
//        DetailFragment frag = new DetailFragment();
//        frag.setArguments(bundle);


//        titleView.setText(title);
//        ratingView.setText(rating);
//        Picasso.with(this).load(poster).into(posterIMG);

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                userRating = ratingBar.getRating();
//
//            }
//        });

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}