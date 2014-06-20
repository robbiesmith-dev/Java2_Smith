package com.example.week3.app.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.week3.app.Activities.DetailActivity;
import com.example.week3.app.Activities.MainActivity;
import com.example.week3.app.Helpers.MovieData;
import com.example.week3.app.Helpers.MovieDataAdapter;
import com.example.week3.app.Helpers.ReadWriteLocalFile;
import com.example.week3.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robertsmith on 6/19/14.
 */
public class MainFragment extends ListFragment implements AdapterView.OnItemClickListener {


    public ListView mList;
    public TextView mListTypeText;
    private static ReadWriteLocalFile fileManager;
    public static String fileName = "JSON_String.txt";
    public static ArrayList<MovieData> MovieData_List;
    public static Context mContext;
    public static MovieDataAdapter adapter;

    private OnMovieSelectedListener mListener;

    public interface OnMovieSelectedListener {
        public void startActivityForResult(Intent data, int RequestCode);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mList = (ListView) getView().findViewById(R.id.list_item);
        mListTypeText = (TextView) getView().findViewById(R.id.listTypeText);
        mListTypeText.setText(R.string.boxOfficeText);

        updateList();

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        DetailFragment frag = (DetailFragment)getFragmentManager().findFragmentById(R.id.fragment_detail);

        //holder vars for MovieData properties
        String title, rating, poster, link;

        title = MovieData_List.get(i).movieName;
        rating = MovieData_List.get(i).rating;
        poster = MovieData_List.get(i).posterURL;
        link = MovieData_List.get(i).rtURL;



        //Intent that will pass properties to detail activity
        Intent detailIntent = new Intent(MainActivity.mContext, DetailActivity.class);
        detailIntent.putExtra("title", title);
        detailIntent.putExtra("rating", rating);
        detailIntent.putExtra("poster", poster);
        detailIntent.putExtra("link", link);

        if(frag != null && frag.isInLayout())
        {
            frag.displayMovieInfo(title, rating, poster, link);
        }
        else
        {
            mListener.startActivityForResult(detailIntent, 0);
        }
    }

    public static void updateList()
    {
            fileManager = ReadWriteLocalFile.getInstance();

            String response = fileManager.readFile(MainActivity.mContext, fileName);

            try
            {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray movies = jsonResponse.getJSONArray("movies");
                MovieData_List = new ArrayList<MovieData>(10);
                //Log.e("TAG", "Movies: " + movies);

                String titlec,ratingc,posterc,linkc;
                for (int i = 0; i < movies.length(); i++)
                {
                    JSONObject movie = movies.getJSONObject(i);
                    titlec = movie.optString("title");
                    ratingc = movie.optString("mpaa_rating");
                    JSONObject posters = new JSONObject(movie.getString("posters"));
                    posterc = posters.optString("profile");
                    JSONObject rtLink  = new JSONObject(movie.getString("links"));
                    linkc = rtLink.optString("alternate");
                    MovieData film = new MovieData(titlec, ratingc, posterc, linkc);

                    Log.e("Array", "is" + titlec);

                    MovieData_List.add(film);
                    Log.e("Array 2", MovieData_List.get(i).getTitle());
                }
                Log.e("BAdpt", MovieData_List.get(4).getTitle());
                adapter = new MovieDataAdapter(MainActivity.mContext, R.layout.list_item, MovieData_List);
                //adapter.setContent(MovieData_List);
            }
            catch (JSONException e)
            {
                Log.e("TAG", "ERROR: " + e);
            }
        }
}
