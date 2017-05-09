package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
import com.jonasgerdes.schauburgr.model.tmdb.entity.video.Video;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private static final String VIDEO_SITE_YOUTUBE = "YouTube";
    private static final Object BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v=";
    @Inject
    MovieRepository mMovieRepository;

    @Inject
    UrlProvider mUrlProvider;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    private MovieDetailContract.View mView;
    private String mVideoUrl;

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
                .subscribe(mView::showMovie));

        //load trailer/teaser
        mDisposables.add(movieObservable
                .doOnNext(mMovieRepository::loadVideos)
                .map(Movie::getVideos)
                .flatMapIterable(videos -> videos)
                .filter(video -> video.getSite().equals(VIDEO_SITE_YOUTUBE))
                .firstElement()
                .map(Video::getKey)
                .map(key -> String.format("%s%s", BASE_URL_YOUTUBE, key))
                .subscribe(url -> {
                    mVideoUrl = url;
                    mView.displayTrailerLink();
                }));
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

    @Override
    public void onTrailerLinkClicked() {
        mView.showTrailer(mVideoUrl);
    }
}
