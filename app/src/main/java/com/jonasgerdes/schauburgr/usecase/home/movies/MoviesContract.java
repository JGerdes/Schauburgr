package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

/**
 * Created by jonas on 05.03.2017.
 */

public interface MoviesContract {
    interface View extends BaseView<MoviesContract.Presenter> {
        void showError(String message);

    }

    interface Presenter extends BasePresenter {
        void loadMovies();
        void stop();
    }
}