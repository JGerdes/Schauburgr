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
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.MovieCategory;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieHolder;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieCategoryView;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieListAdapter;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

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
    private CompositeDisposable mDisposables = new CompositeDisposable();
    /**
     * Whether opening of a detail activity is currently in process. Since it takes a moment to load
     * poster with glide, user can click fast leading to multiple instances of
     * {@link MovieDetailActivity} to be opened
     */
    private boolean mIsOpeningDetails = false;

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
    public void onResume() {
        mIsOpeningDetails = false;
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mDisposables.dispose();
        mPresenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void addMovieCategory(Observable<MovieCategory> category) {
        MovieCategoryView movieCategoryView = new MovieCategoryView(getContext());
        movieCategoryView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        movieCategoryView.setMovieSelectedListener(this);
        mCategoryContainer.addView(movieCategoryView);
        mDisposables.add(
                category.subscribe(movieCategoryView::bindCategory)
        );
    }

    @Override
    public void showMovieCategories(List<Observable<MovieCategory>> categories) {
        categories.forEach(this::addMovieCategory);
    }

    @Override
    public void onMovieClicked(Movie movie, MovieHolder holder) {
        //prevent opening details twice
        if (!mIsOpeningDetails) {
            mIsOpeningDetails = true;
            MovieDetailActivity.start(getActivity(), movie, holder.getPosterView());
        }
    }
}
