package com.jonasgerdes.schauburgr.usecase.home.movies;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.usecase.home.HomeView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesView extends FrameLayout implements HomeView, MoviesContract.View {
    private MoviesContract.Presenter mPresenter;


    public MoviesView(@NonNull Context context) {
        super(context);
        init();
    }

    public MoviesView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoviesView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MoviesView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_movies, this);
        ButterKnife.bind(this);
        new MoviesPresenter(this);

    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        mPresenter.loadMovies();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void showMovies(List<Movie> movies) {

    }
}
