package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.network.url.UrlProvider;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    @Inject
    Realm mRealm;

    @Inject
    UrlProvider mUrlProvider;

    private MovieDetailContract.View mView;

    @Override
    public void attachView(MovieDetailContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void detachView() {
        mRealm.removeAllChangeListeners();
        mRealm.close();
    }


    @Override
    public void onStartWithMovieId(String movieId) {
        Movie movie = mRealm.where(Movie.class)
                .equalTo("resourceId", movieId)
                .findFirst();
        mView.showMovie(movie);
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        String url = mUrlProvider.getReservationPageUrl(screening);
        mView.openWebpage(url);
    }
}
