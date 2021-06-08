package com.example.flixster.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//
//Purpose:          To encapsulate all relevant information aout each movie. Can parse out information given a JSONObject representing a movie.
public class Movie {
    private static final String TAG = "Movie";
    //Fields:
    String posterPath;
    String backdropPath;
    String title;
    String overview;

    String posterSize;
    String backdropSize;

    //Purpose:      Takes a JSONObject that represents 1 movie, and saves the relevant data. Takes the desired image sizes to save for later.
    public Movie(JSONObject jsonObject, String posterSize, String backdropSize) throws JSONException {
        Log.d(TAG, "Movie():    posterSize=" + posterSize + "  backdropSize = " + backdropSize);
        this.posterSize = posterSize;
        this.backdropSize = backdropSize;

        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
    }

    //Purpose:      Take a JSONArray to create and return a list of Movies. This is static so the HomeActivity can call this.
    public static List<Movie> fromJsonArray(JSONArray jsonMovies, String posterSize, String backdropSize) throws JSONException {
        Log.d(TAG, "fromJsonArray():    posterSize=" + posterSize + "  backdropSize = " + backdropSize);
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < jsonMovies.length(); i++){
            movies.add(new Movie(jsonMovies.getJSONObject(i), posterSize, backdropSize));
        }
        return movies;
    }

    //Returns the actual usable file path to upload into Glide and displays a picture --> uses the saved posterSize
    public String getPosterPath() {
        Log.d(TAG, "getPosterPath(): file=" + String.format("https://image.tmdb.org/t/p/%s%s", posterSize, posterPath));
        return String.format("https://image.tmdb.org/t/p/%s%s", posterSize, posterPath);
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    //Returns the actual usable file path to upload into Glide and displays a picture --> uses the saved posterSize
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/%s%s", backdropSize, backdropPath);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
}
