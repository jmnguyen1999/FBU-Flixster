package com.example.flixster.network;

//Purpose:          This class is responsible for all network requests from this app! Makes the necessary requests to "themoviedb.org" for now playing movie information, image configuration information, and YouTube trailer information.

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

public class MovieDBClient {
    private static final String TAG ="MovieDBClient";

    // Keys + Base url:
    private static final String API_KEY="61bde20d47417bf3a0adf1ab1eb5b898";
    private static final String BASE_URL="https://api.themoviedb.org/3/";

    //Endpoints neccessary;
    private static final String NOW_PLAYING_ENDPOINT="movie/now_playing?api_key=";
    private static final String CONFIGURATION_ENDPOINT="configuration?api_key=";
    private static final String VIDEO_ENDPOINT="movie/%d/videos?api_key=";  //I think needs a number? Unsure rn

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void makeNowPlayingReqest(JsonHttpResponseHandler handler) {
        client.get(BASE_URL + NOW_PLAYING_ENDPOINT + API_KEY, handler);
    }

    public static void makeImageSizeRequest(JsonHttpResponseHandler handler){
        client.get(BASE_URL+CONFIGURATION_ENDPOINT+API_KEY, handler);
    }
}
