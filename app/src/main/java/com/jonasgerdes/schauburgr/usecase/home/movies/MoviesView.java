package com.jonasgerdes.schauburgr.usecase.home.movies;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.usecase.home.HomeView;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesView extends FrameLayout implements HomeView, MoviesContract.View {
    private MoviesContract.Presenter mPresenter;

    @BindView(R.id.movieList)
    RecyclerView mMovieList;

    private MovieListAdapter mMovieListAdapter;
    private Realm mRealm;

    public MoviesView(@NonNull Context context) {
        super(context);
        init();
    }

    public MoviesView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoviesView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MoviesView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void showError(String message) {

    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_movies, this);
        ButterKnife.bind(this);
        new MoviesPresenter(this);
        mMovieListAdapter = new MovieListAdapter();
        mMovieList.setAdapter(mMovieListAdapter);
        mMovieList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        bindModel();

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
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }
}
