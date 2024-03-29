package com.example.week3.app.Fragments;

//Robert Smith
//Java 2 Project 3 Term 1406
//June 12 2014

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.week3.app.R;
import com.squareup.picasso.Picasso;

/**
 * Created by robertsmith on 6/19/14.
 */
public class DetailFragment extends Fragment {

    ImageView posterIMG;
    TextView titleView, ratingView;
    Button rtButton;
    RatingBar ratingBar;

    public String title;
    public String rating;
    public String poster;
    public String link;

//    OnWebsiteClick mListener;
//
//    public interface OnWebsiteClick {
//        public void WebsiteClicked();
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnWebsiteClick) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        titleView = (TextView)getView().findViewById(R.id.titleView);
        ratingView = (TextView)getView().findViewById(R.id.ratingView);
        posterIMG = (ImageView)getView().findViewById(R.id.posterView);
        rtButton =(Button)getView().findViewById(R.id.rtButton);
        ratingBar = (RatingBar)getView().findViewById(R.id.ratingBar);

//        rtButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v)
//            {
//                mListener.WebsiteClicked();
//            }
//        });
    }

    public void displayMovieInfo(String title, String rating, String poster, String link)
    {
        titleView.setText(title);
        ratingView.setText(rating);
        Picasso.with(getActivity()).load(poster).into(posterIMG);
    }
}
