package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.model.MovieCategory;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public interface MoviesContract {
    interface View extends BaseView<MoviesContract.Presenter> {
        void addMovieCategory(MovieCategory category);
        void showMovieCategories(List<MovieCategory> categories);

    }

    interface Presenter extends BasePresenter<MoviesContract.View> {
    }
}