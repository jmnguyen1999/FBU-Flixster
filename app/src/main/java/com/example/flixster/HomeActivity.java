package com.example.flixster;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;
import com.example.flixster.network.MovieDBClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * HomeActivity.java
 * Purpose:             This is the main screen of the app to display the list of movies!
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private List<Movie> movies;
    private String posterSize = "";
    private String backdropSize = "";
    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        rvMovies = findViewById(R.id.rvMovies);
        movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //fetchMovies();
        fetchImageSizes();
    }

    public void fetchMovies(){
        //fetchImageSizes();          //initializes posterSize and backdropSize fields

        MovieDBClient.makeNowPlayingReqest(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.i(TAG, "successful request for movies");

                //1.) Get the "results" array from the json object!
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");

                    //2.) Use static method from Movie{} to parse this JSOArray to a List<Movie> instead:
                    movies.addAll(Movie.fromJsonArray(results, posterSize, backdropSize));
                    movieAdapter.notifyDataSetChanged();

                    Log.i(TAG, "results received=" + movies.toString());

                } catch (JSONException e) {
                    Log.e(TAG, "json exception while getting \"results\" array: error=", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.i(TAG, "failed request for movies");
            }
        });
    }

    //Purpose:          Makes request or image configurations available for poster and backdrop images. Just chooses the size that is in middle/medium list by default. This is called by fetchMovies() to initalize the posterSize and backdropSize fields before finding and initializing Movie objects.
    public void fetchImageSizes(){
        Log.d(TAG, "fetchImageSizes()");

        MovieDBClient.makeImageSizeRequest(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.i(TAG, "successful request for image sizes, json=" + json.jsonObject.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject imagesObject = jsonObject.getJSONObject("images");
                    JSONArray allBackdropSizes = imagesObject.getJSONArray("backdrop_sizes");
                    JSONArray allPosterSizes = imagesObject.getJSONArray("poster_sizes");
                    posterSize = allPosterSizes.get(allPosterSizes.length()/2).toString();
                    backdropSize = allBackdropSizes.get(allBackdropSizes.length()/2).toString();
                    Log.d(TAG, "posterSize = " + posterSize + "   backdropSize = " + backdropSize);

                    fetchMovies();
                } catch (JSONException e) {
                    Log.e(TAG, "error getting arrays =", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.i(TAG, "failed request for image sizes");
            }
        });
    }
}