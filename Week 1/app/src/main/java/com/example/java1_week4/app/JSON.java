package com.example.java1_week4.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by robertsmith on 6/2/14.
 */
public class JSON {

    public static final String TAG = JSON.class.getSimpleName();
    public static String[] boxOfficeMovies = new String[10];
    public static String[] rentalMovies = new String[10];



    public static String[] getBoxOfficeJSON(String s)
    {
        try
        {
            JSONObject jsonResponse = new JSONObject(s);
            JSONArray movies = jsonResponse.getJSONArray("movies");
            Log.e("TAG", "Movies: " + movies);

            for (int i = 0; i < movies.length(); i++)
            {
                JSONObject movie = movies.getJSONObject(i);
                String title = movie.getString("title");
                String rating = movie.getString("mpaa_rating");

                boxOfficeMovies[i] = title;

            }
        }
        catch (JSONException e)
        {
            Log.e("TAG", "ERROR: " + e);
        }

        return boxOfficeMovies;
    }

    public static String[] getRentalJSON(String s)
    {
        try
        {
            JSONObject jsonResponse = new JSONObject(s);
            JSONArray movies = jsonResponse.getJSONArray("movies");
            Log.e("TAG", "Movies: " + movies);

            for (int i = 0; i < movies.length(); i++)
            {
                JSONObject movie = movies.getJSONObject(i);
                String title = movie.getString("title");
                String rating = movie.getString("mpaa_rating");

                rentalMovies[i] = title;
            }
        }
        catch (JSONException e)
        {
            Log.e("TAG", "ERROR: " + e);
        }

        return rentalMovies;
    }
}

