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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertsmith on 6/11/14.
 */
public class MovieDataAdapter extends ArrayAdapter<MovieData>
{
    Context context;
    int layoutResourceId;
    ArrayList<MovieData> MovieData_List;

    public MovieDataAdapter(Context context, int layoutResourceId, ArrayList<MovieData> MovieData_List) {
        super(context, layoutResourceId, MovieData_List);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.MovieData_List = MovieData_List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieDataHolder holder = null;

        MovieData movie = getItem(position);

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

        //Set values to visual elements in list view
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
