package com.jonasgerdes.schauburgr.usecase.home.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesView extends Fragment implements MoviesContract.View {
    private MoviesContract.Presenter mPresenter;

    @BindView(R.id.movieList)
    RecyclerView mMovieList;

    private MovieListAdapter mMovieListAdapter;
    private Realm mRealm;

    public static MoviesView newInstance() {
        Bundle args = new Bundle();

        MoviesView fragment = new MoviesView();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.home_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        new MoviesPresenter(this);
        mMovieListAdapter = new MovieListAdapter();
        mMovieList.setAdapter(mMovieListAdapter);
        mMovieList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        bindModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRealm.close();
    }

    private void bindModel() {
        mRealm = Realm.getDefaultInstance();
        RealmResults<Movie> movies = mRealm.where(Movie.class).findAll();
        mMovieListAdapter.setMovies(movies);
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showError(String message) {

    }
}
