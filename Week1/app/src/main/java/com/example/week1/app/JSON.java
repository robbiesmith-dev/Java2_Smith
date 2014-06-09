package com.example.week1.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//Robert Smith
//Java 2 Project 1 Term 1406
//June 7 2014
//JSON Class - Formats the JSON into String Arrays so they can be displayed as List Items in MainActivity

public class JSON {

    public static final String TAG = JSON.class.getSimpleName();
    public static String[] boxOfficeMovies = new String[10];
    public static String[] rentalMovies = new String[10];



    public static ArrayList getBoxOfficeJSON(String s)
    {

        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
        try
        {
            JSONObject jsonResponse = new JSONObject(s);
            JSONArray movies = jsonResponse.getJSONArray("movies");
            //Log.e("TAG", "Movies: " + movies);

            for (int i = 0; i < movies.length(); i++)
            {
                JSONObject movie = movies.getJSONObject(i);
                String title = movie.getString("title");
                String rating = movie.getString("mpaa_rating");
                String year = movie.getString("year");

                HashMap<String, String> displayMap = new HashMap<String, String>();
                displayMap.put("title", title);
                displayMap.put("rating", rating);
                displayMap.put("year", year);

                myList.add(displayMap);
            }
        }
        catch (JSONException e)
        {
            Log.e("TAG", "ERROR: " + e);
        }

        return myList;
    }
}
