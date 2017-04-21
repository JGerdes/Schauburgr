package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public interface MovieDetailContract {
    interface View extends BaseView<MovieDetailContract.Presenter> {
        void showMovie(Movie movie);
        void showScreenings(RealmResults<Screening> screenings);

    }

    interface Presenter extends BasePresenter<MovieDetailContract.View> {
        void onStartWithMovieId(String movieId);
    }
}