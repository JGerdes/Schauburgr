package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public interface MoviesContract {
    interface View extends BaseView<MoviesContract.Presenter> {
        void showError(String message);
        void showMovies(RealmResults<Movie> movies);

    }

    interface Presenter extends BasePresenter {
        void loadMovies();
        void stop();
    }
}