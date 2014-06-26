package com.example.week4.app;

//Robert Smith
//Java 2 Project 2 Term 1406
//June 12 2014
//Movie Data Adapter  -  Custom implementation of rows in mainactivities list view

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class MovieDataAdapter extends ArrayAdapter<MovieData>
{
    Context context;
    int layoutResourceId;
    public static ArrayList<MovieData> MovieData_List;
    public static ArrayList<MovieData> arraylist;

    public MovieDataAdapter(Context context, int layoutResourceId, ArrayList<MovieData> MovieData_List) {
        super(context, layoutResourceId, MovieData_List);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.MovieData_List = MovieData_List;
        this.arraylist = new ArrayList<MovieData>();
        this.arraylist.addAll(MovieData_List);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieDataHolder holder = null;

        MovieData movie = getItem(position);

        //This quick check improves performace by checking if the cell has been created already
        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            //Get view elements from XML file
            holder = new MovieDataHolder();
            holder.posterView = (ImageView) row.findViewById(R.id.poster);
            holder.titleView = (TextView) row.findViewById(R.id.title);
            holder.ratingView = (TextView) row.findViewById(R.id.rating);

            //Set tag for else statement
            row.setTag(holder);
        } else
        {
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

    //FILTERS LIST BASED ON TEXT INPUT
    public void search(String searchText)
    {
        //INPUT TEXT CONVERTED TO LOWERCASE
        searchText = searchText.toLowerCase();
        //EMPTY MOVIE LIST
        MovieData_List.clear();
        //IF EDITTEXT IS EMPTY RETURN ORIGINAL LIST
        if (searchText.length() == 0)
        {
            MovieData_List.addAll(arraylist);
            Log.e("LENGTH 0", "" + MovieData_List.get(0).getTitle());
        }
        else
        {
            //ITERATE THROUGH LIST
            for (MovieData movie : arraylist)
            {
                //IF TEXT MATCHES
                if (movie.getTitle().toLowerCase().contains(searchText))
                {
                    Log.e("ADDED MOVIE", "" + movie.getTitle());
                    //ADD MOVIE
                    MovieData_List.add(movie);
                }
            }
        }
        if(MovieData_List.isEmpty())
        {
            MovieData_List.addAll(arraylist);
            Toast.makeText(context, "No Results, Try Searching Again", Toast.LENGTH_LONG).show();
        }
        //RESET THE LIST
        notifyDataSetChanged();
    }
}
