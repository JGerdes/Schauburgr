package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.model.schauburg.entity.MovieCategory;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jonas on 05.03.2017.
 */

public interface MoviesContract {
    interface View extends BaseView<MoviesContract.Presenter> {
        void addMovieCategory(Observable<MovieCategory> category);
        void showMovieCategories(List<Observable<MovieCategory>> categories);

    }

    interface Presenter extends BasePresenter<MoviesContract.View> {
    }
}