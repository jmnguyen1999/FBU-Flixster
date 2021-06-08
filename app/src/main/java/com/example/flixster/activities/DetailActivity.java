package com.example.flixster.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityDetailBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private static final String KEY_MOVIE_RECEIVED = "movieMoreDetail";     //Key used to receive Movie from HomeActivity

    //Views:
    ImageView ivTrailer;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;

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

        //2.) Get data from HomeActivity:
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra(KEY_MOVIE_RECEIVED));

        //3.) Fill in the views with the received Movie!
        Glide.with(this).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivTrailer);
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());
    }
}