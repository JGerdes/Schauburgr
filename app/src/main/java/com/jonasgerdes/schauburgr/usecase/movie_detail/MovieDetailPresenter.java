package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.network.url.UrlProvider;

import java.util.Date;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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
        //sometimes there are same movies with different extras (3D, atmos)
        //find all screenings of movies regardless of extra-splitting
        RealmResults<Screening> screenings = mRealm.where(Screening.class)
                .equalTo("movie.title", movie.getTitle())
                .greaterThan("startDate", new Date())
                .findAllSorted("startDate", Sort.ASCENDING);
        mView.showScreenings(screenings);
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        String url = mUrlProvider.getReservationPageUrl(screening);
        mView.openWebpage(url);
    }
}
