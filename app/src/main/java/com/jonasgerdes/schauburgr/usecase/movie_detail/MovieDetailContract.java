package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
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
        void openWebpage(String url);
        void displayTrailerLink();
        void enableCoverScrim(boolean enabled);
        void showTrailer(String url);

    }

    interface Presenter extends BasePresenter<MovieDetailContract.View> {
        void onStartWithMovieId(String movieId);
        void onScreeningSelected(Screening screening);
        void onTrailerLinkClicked();
    }
}