package com.jonasgerdes.schauburgr.usecase.home.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.MovieCategory;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.CompactMovieHolder;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieCategoryView;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieListAdapter;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesView extends Fragment implements MoviesContract.View,
        MovieListAdapter.MovieClickedListener {

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.categoryContainer)
    LinearLayout mCategoryContainer;

    private MoviesContract.Presenter mPresenter;

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
        getActivity().setTitle(R.string.title_movies);
        ButterKnife.bind(this, view);

        new MoviesPresenter().attachView(this);
    }


    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void addMovieCategory(MovieCategory category) {
        MovieCategoryView movieCategoryView = new MovieCategoryView(getContext());
        movieCategoryView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        movieCategoryView.bindCategory(category);
        movieCategoryView.setMovieSelectedListener(this);
        mCategoryContainer.addView(movieCategoryView);
    }

    @Override
    public void showMovieCategories(List<MovieCategory> categories) {
        for (MovieCategory category : categories) {
            addMovieCategory(category);
        }
    }

    @Override
    public void onMovieClicked(Movie movie, CompactMovieHolder holder) {
        MovieDetailActivity.start(getActivity(), movie, holder.getPosterView());
    }
}
