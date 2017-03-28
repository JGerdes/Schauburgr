package com.jonasgerdes.schauburgr.usecase.home.movies.movie_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieHolder> {

    private List<Movie> mMovies = new ArrayList<>();

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_movies_item_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.onBind(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setMovies(RealmResults<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
        movies.addChangeListener(new RealmChangeListener<RealmResults<Movie>>() {
            @Override
            public void onChange(RealmResults<Movie> element) {
                notifyDataSetChanged();
            }
        });
    }
}
