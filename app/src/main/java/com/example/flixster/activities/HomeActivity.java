package com.example.flixster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityHomeBinding;
import com.example.flixster.models.Movie;
import com.example.flixster.network.MovieDBClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * HomeActivity.java
 * Purpose:             This is the main screen of the app to display the list of movies!
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final String KEY_MOVIE_SEND = "movieMoreDetail";     //key used to send a Movie to DetailActivity

    private List<Movie> movies;
    private String posterSize = "";
    private String backdropSize = "";
    private RecyclerView rvMovies;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.) Initialize our layout (taken care of by the auto-generated ActivityHomeBinding class)
        // all views in activity_home.xml are stored as fields now!
        ActivityHomeBinding binder = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());       //getRoot() --> returns layout file

        movies = new ArrayList<>();
        rvMovies = binder.rvMovies;

        //2.) Create an MovieAdapter.OnclickListener object to go into the constructor + instantiate a MoviesAdapter object:
        MovieAdapter.OnClickListener onClickListener = new MovieAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                //When a movie row is clicked --> go to DetailActivity!
                Intent toDetailActivity = new Intent(HomeActivity.this, DetailActivity.class);

                //Get the movies at the position that was clicked, wrap it up, and put it into this Intent instance:
                toDetailActivity.putExtra(KEY_MOVIE_SEND, Parcels.wrap(movies.get(position)));
                startActivity(toDetailActivity);
            }
        };
        movieAdapter = new MovieAdapter(this, movies, onClickListener);

        //3.) Set up the RecyclerView:
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //4.) Fetch movie and image size data!
        fetchImageSizes();
    }

    //Purpose:          Makes a request for current now playing movies using a static method from MovieDBClient. Passes in the result to a static method in Movies to parse the JSON data into a List<Movie> instead. Assumes posterSize and backdropSize are initialized.
    public void fetchMovies(){
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