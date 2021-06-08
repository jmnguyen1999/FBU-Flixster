package com.example.flixster.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;
import com.example.flixster.network.MovieDBClient;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String TAG = "DetailActivity";
    private static final String YOUTUBE_API_KEY= "AIzaSyAWq6FD7HNjxKYVkbsGJ9IZSoF63hFVLLk";
    private static final String KEY_MOVIE_RECEIVED = "movieMoreDetail";     //Key used to receive Movie from HomeActivity

    //Views:
    ImageView ivTrailer;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youtubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.) Initialize our layout (taken care of by the auto-generated ActivityHomeBinding class)
        // all views in activity_home.xml are stored as fields now!
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());          //getRoot() --> returns layout file

        //1b.) Connect Views:
        ivTrailer = binding.ivTrailer;
        tvTitle = binding.tvDetailTitle;
        tvOverview = binding.tvDetailOverview;
        ratingBar = binding.ratingBar;
        youtubePlayerView = binding.youtubePlayer;

        //2.) Get data from HomeActivity:
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra(KEY_MOVIE_RECEIVED));

        //3.) Fill in the views with the received Movie!
        Glide.with(this).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivTrailer);
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());

        //4.) Make a request to find all trailers for this movie --> initialize youtuubePlayerView with video
        String movieId = movie.getId();
        MovieDBClient.makeVideoRequest(movieId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject receivedJson= json.jsonObject;
                try{
                    JSONArray results = receivedJson.getJSONArray("results");
                    //Just get the first video --> Official trailer:
                    JSONObject officialTrailer = (JSONObject) results.get(0);
                    String trailerKey = officialTrailer.getString("key");
                    initializeYoutubePlayer(trailerKey);
                }catch(JSONException e){
                    Log.e(TAG, "exception for getting list of videos for movie id = " + movieId, e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG, "Failed to get list of videos", throwable);
            }
        });
    }

    //Purpose:      Loads a given Youtube video into the youtubePlayerView
    private void initializeYoutubePlayer(String videoKey){
        youtubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoKey);       //cueVideo() --> loads but doesn't play automatically
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.e(TAG, "YoutubePlayer failed to initialize: result=" + youTubeInitializationResult);
                    }
        });
    }
}