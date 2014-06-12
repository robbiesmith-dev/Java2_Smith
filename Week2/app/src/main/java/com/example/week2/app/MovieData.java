package com.example.week2.app;

/**
 * Created by robertsmith on 6/11/14.
 */
public class MovieData {

    public static String movieName;
    public static String rating;
    public static String posterURL;

    public MovieData(){
        super();
    }

    public MovieData(String movieName, String rating, String posterURL){
        super();
        this.movieName = movieName;
        this.rating = rating;
        this.posterURL = posterURL;
    }
}
