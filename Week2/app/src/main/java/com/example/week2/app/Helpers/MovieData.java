package com.example.week2.app.Helpers;

import java.io.Serializable;

//Robert Smith
//Java 2 Project 2 Term 1406
//June 12 2014
//Movie Data -  Custom object for easy passing of multiple properties linked to a particular movie

public class MovieData implements Serializable{

    public  String movieName;
    public  String rating;
    public  String posterURL;
    public  String rtURL;

    public MovieData(){
        super();
    }

    public MovieData(String movieName, String rating, String posterURL, String rtURL){
        super();
        this.movieName = movieName;
        this.rating = rating;
        this.posterURL = posterURL;
        this.rtURL = rtURL;
    }

    public String getTitle() {
        return movieName;
    }
}
