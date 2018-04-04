package com.example.android.popularmoviesstage2.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 26.02.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private ArrayList<Movie> movieList;
    private final OnItemClickListener listener;

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    /**
     * Create a new {@link MovieAdapter} object.
     *
     * @param movieList is the list of {@link Movie}s to be displayed.
     * @param listener  is an object of OnItemClickListener.
     */
    public MovieAdapter(ArrayList<Movie> movieList, OnItemClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);
        String finalPosterUrl = getImageUrl(BASE_POSTER_URL, movie.getMovieImagePosterPath());
        Picasso.with(holder.itemView.getContext())
                .load(finalPosterUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(holder.posterView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList == null || movieList.isEmpty()) {
            return 0;
        } else {
            return this.movieList.size();
        }
    }

    // Create the ViewHolder class for references
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_poster)
        ImageView posterView;

        // Public constructor, instantiate all of the references to the private variables
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Helper method to set new movie list or clear the previous one
    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    // Helper method to get the full image URL as a string
    private String getImageUrl(String baseUrl, String posterPath) {

        Uri baseUri = Uri.parse(baseUrl);
        Uri.Builder posterUri = baseUri.buildUpon();
        posterUri.appendEncodedPath(posterPath);

        return posterUri.toString();
    }
}

