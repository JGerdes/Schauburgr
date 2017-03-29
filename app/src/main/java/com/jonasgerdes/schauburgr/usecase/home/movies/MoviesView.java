package com.jonasgerdes.schauburgr.usecase.home.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesView extends Fragment implements MoviesContract.View, SwipeRefreshLayout
        .OnRefreshListener {
    private MoviesContract.Presenter mPresenter;

    @BindView(R.id.movieList)
    RecyclerView mMovieList;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private MovieListAdapter mMovieListAdapter;
    private Realm mRealm;
    private Animation mUpdateAnimation;

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
        new MoviesPresenter(this);
        mMovieListAdapter = new MovieListAdapter();
        mMovieList.setAdapter(mMovieListAdapter);
        mMovieList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );

        mRefreshLayout.setOnRefreshListener(this);
        int triggerDistance = getContext()
                .getResources().getDimensionPixelSize(R.dimen.swipe_refresh_trigger_distance);
        mRefreshLayout.setDistanceToTriggerSync(triggerDistance);

        mUpdateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);

        bindModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRefreshLayout.setOnRefreshListener(null);
        mMovieList.clearAnimation();
        mPresenter.stop();
        mRealm.close();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadMovies();
    }

    private void bindModel() {
        mRealm = Realm.getDefaultInstance();
        RealmResults<Movie> movies = mRealm.where(Movie.class).findAll();
        mMovieListAdapter.setMovies(movies);
        movies.addChangeListener(new RealmChangeListener<RealmResults<Movie>>() {
            @Override
            public void onChange(RealmResults<Movie> element) {
                mRefreshLayout.setRefreshing(false);
                mMovieList.startAnimation(mUpdateAnimation);
            }
        });
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showError(String message) {
        mRefreshLayout.setRefreshing(false);
    }
}
