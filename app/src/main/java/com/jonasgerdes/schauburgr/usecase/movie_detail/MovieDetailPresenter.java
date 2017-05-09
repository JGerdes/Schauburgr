package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    @Inject
    MovieRepository mMovieRepository;

    @Inject
    UrlProvider mUrlProvider;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    private MovieDetailContract.View mView;

    @Override
    public void attachView(MovieDetailContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void detachView() {
        mDisposables.dispose();
    }


    @Override
    public void onStartWithMovieId(String movieId) {
        Observable<Movie> movieObservable = mMovieRepository.getMovieById(movieId);
        mDisposables.add(movieObservable
                .doOnNext(this::loadScreenings)
                .doOnNext(mMovieRepository::loadVideos)
                .subscribe(mView::showMovie));
    }

    private void loadScreenings(Movie movie) {
        Observable<RealmResults<Screening>> screenings
                = mMovieRepository.getAllScreeningsFor(movie);
        mDisposables.add(screenings.subscribe(mView::showScreenings));
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        String url = mUrlProvider.getReservationPageUrl(screening);
        mView.openWebpage(url);
    }
}
