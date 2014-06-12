package com.example.week2.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by robertsmith on 6/11/14.
 */
public class MovieDataAdapter extends ArrayAdapter<MovieData>
{
    Context context;
    int layoutResourceId;
    MovieData movies[] = null;

    public MovieDataAdapter(Context context, int layoutResourceId, MovieData[] movies) {
        super(context, layoutResourceId, movies);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieDataHolder holder = null;

        //This quick check improves performace by checking if the cell has been created already
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            //Get view elements from XML file
            holder = new MovieDataHolder();
            holder.posterView = (ImageView) row.findViewById(R.id.poster);
            holder.titleView = (TextView) row.findViewById(R.id.title);
            holder.ratingView = (TextView) row.findViewById(R.id.rating);

            //Set tag for else statement
            row.setTag(holder);
        } else {
            holder = (MovieDataHolder) row.getTag();
        }

        //Set values to visual elements in listview
        MovieData movie = movies[position];
        Picasso.with(context).load(movie.posterURL).into(holder.posterView);
        holder.titleView.setText(movie.movieName);
        holder.ratingView.setText(movie.rating);

        return row;
    }

    //This class will be used as a reference for the view elements in the list_item
    class MovieDataHolder
    {
        ImageView posterView;
        TextView titleView;
        TextView ratingView;
    }
}
