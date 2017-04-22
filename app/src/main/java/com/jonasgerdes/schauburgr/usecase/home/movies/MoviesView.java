package com.jonasgerdes.schauburgr.usecase.home.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.CompactMovieHolder;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieCategoryView;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieListAdapter;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesView extends Fragment implements MoviesContract.View, SwipeRefreshLayout
        .OnRefreshListener, MovieListAdapter.MovieClickedListener {
    private MoviesContract.Presenter mPresenter;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.categoryContainer)
    LinearLayout mCategoryContainer;

    private Animation mUpdateAnimation;
    private Snackbar mSnackbar;

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

        mUpdateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        initRefreshLayout();

        new MoviesPresenter().attachView(this);
    }


    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        int triggerDistance = getContext()
                .getResources().getDimensionPixelSize(R.dimen.swipe_refresh_trigger_distance);
        mRefreshLayout.setDistanceToTriggerSync(triggerDistance);
    }

    @Override
    public void onDestroyView() {
        mRefreshLayout.setOnRefreshListener(null);
        mPresenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showError(String message) {
        mRefreshLayout.setRefreshing(false);
        mSnackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_action_refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRefreshLayout.setRefreshing(true);
                        onRefresh();
                    }
                });
        mSnackbar.show();
    }

    @Override
    public void addMovieCategory(@StringRes int categoryName, RealmResults<Movie> movies) {
        MovieCategoryView movieCategoryView = new MovieCategoryView(getContext());
        movieCategoryView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        movieCategoryView.setTitle(categoryName);
        movieCategoryView.setMovies(movies);
        movieCategoryView.setMovieSelectedListener(this);
        mCategoryContainer.addView(movieCategoryView);
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefreshTriggered();
        hideError();
    }

    private void hideError() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    @Override
    public void onMovieClicked(Movie movie, CompactMovieHolder holder) {
        MovieDetailActivity.start(getActivity(), movie, holder.getPosterView());
    }
}
