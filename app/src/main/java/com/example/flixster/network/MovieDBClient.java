package com.example.flixster.network;

//Purpose:          This class is responsible for all network requests from this app! Makes the necessary requests to "themoviedb.org" for now playing movie information, image configuration information, and YouTube trailer information.

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class MovieDBClient {
    private static final String TAG ="MovieDBClient";

    // Keys + Base url:
    private static final String MOVIE_API_KEY ="61bde20d47417bf3a0adf1ab1eb5b898";
    private static final String BASE_URL="https://api.themoviedb.org/3/";

    //Endpoints neccessary;
    private static final String NOW_PLAYING_ENDPOINT="movie/now_playing?api_key=";
    private static final String CONFIGURATION_ENDPOINT="configuration?api_key=";
    private static final String VIDEO_ENDPOINT="movie/%d/videos?api_key=";      //%d = needs the movie-id

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void makeNowPlayingReqest(JsonHttpResponseHandler handler) {
        client.get(BASE_URL + NOW_PLAYING_ENDPOINT + MOVIE_API_KEY, handler);
    }

    public static void makeImageSizeRequest(JsonHttpResponseHandler handler){
        client.get(BASE_URL+CONFIGURATION_ENDPOINT+ MOVIE_API_KEY, handler);
    }

    public static void makeVideoRequest(String movieId, JsonHttpResponseHandler handler){
        String url = String.format(BASE_URL + VIDEO_ENDPOINT + MOVIE_API_KEY, Integer.parseInt(movieId));
        Log.d(TAG, "makeVideoRequest():  url=" + url);
        client.get(url, handler);
    }
}
