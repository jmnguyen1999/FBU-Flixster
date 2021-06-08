package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private static final String TAG = "MovieAdapter";

    List<Movie> movies;
    Context context;
    ItemMovieBinding binding;

    private OnClickListener onClickListener;
    public interface OnClickListener{
        void onClick(int position);
    }

    public MovieAdapter(Context context, List<Movie> movies, OnClickListener onClickListener){
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivPoster;
        RelativeLayout rvContainer;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = binding.tvTitle;
            tvDescription = binding.tvDescription;
            ivPoster = binding.ivPoster;
            rvContainer = binding.rvContainer;
        }

        public void bind(Movie movie, int postion) {
            //1.) Tie a Click listener to the entire container --> onClick() in the MovieAdapter.OnClickListener:
            rvContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(postion);
                }
            });

            //2.) Tie data to the views from this given Movie:
            tvTitle.setText(movie.getTitle());
            tvDescription.setText(movie.getOverview());

            //2b.) Figure out which orientation in:
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d(TAG, "poster path = " + movie.getPosterPath());
                Glide.with(context)
                        .load(movie.getPosterPath())
                        .placeholder(R.drawable.flicks_movie_placeholder)
                        .transform(new RoundedCornersTransformation(30, 5))
                                .into(ivPoster);
            } else {
                Log.d(TAG, "backdrop path = " + movie.getBackdropPath());
                Glide.with(context)
                        .load(movie.getBackdropPath())
                        .placeholder(R.drawable.flicks_backdrop_placeholder)
                        .transform(new RoundedCornersTransformation(30, 10))
                        .into(ivPoster);
            }
        }
    }
}
