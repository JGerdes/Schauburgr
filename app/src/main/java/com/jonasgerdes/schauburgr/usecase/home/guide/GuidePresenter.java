package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.util.Log;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.model.tmdb.search.SearchResult;
import com.jonasgerdes.schauburgr.network.SchauburgApi;
import com.jonasgerdes.schauburgr.network.tmdb.TheMovieDatabaseApi;
import com.jonasgerdes.schauburgr.network.url.UrlProvider;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuidePresenter implements GuideContract.Presenter {

    @Inject
    SchauburgApi mSchauburgApi;

    @Inject
    TheMovieDatabaseApi mTMDbApi;

    @Inject
    UrlProvider mUrlProvider;

    @Inject
    Realm mRealm;

    private GuideContract.View mView;
    private CompositeDisposable mDisposables;
    private RealmResults<ScreeningDay> mScreeningDays;
    private boolean mDoAnimateNewData = false;

    private RealmChangeListener<RealmResults<ScreeningDay>> mChangeListener = screeningDays -> {
        mView.showScreeningDays(screeningDays, mDoAnimateNewData);
        mDoAnimateNewData = false;
    };

    @Override
    public void attachView(GuideContract.View view) {
        App.getAppComponent().inject(this);
        mDisposables = new CompositeDisposable();
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void detachView() {
        mDisposables.dispose();
        mScreeningDays.removeAllChangeListeners();
        mRealm.close();
    }

    @Override
    public void onRefreshTriggered() {
        //show user new data with animation
        mDoAnimateNewData = true;
        fetchGuideData();
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        //only open reservation page if screening hasn't started yet
        if (screening.getStartDate().isAfter(new DateTime())) {
            String url = mUrlProvider.getReservationPageUrl(screening);
            mView.openWebpage(url);
        }
    }

    public void loadGuide(boolean forceRefresh) {
        mScreeningDays = mRealm.where(ScreeningDay.class)
                .greaterThanOrEqualTo("date", new LocalDate().toDate())
                .findAllSorted("date", Sort.ASCENDING);
        mChangeListener.onChange(mScreeningDays);
        mScreeningDays.addChangeListener(mChangeListener);

        if (Realm.getDefaultInstance().where(ScreeningDay.class).count() == 0) {
            //show animation on first load
            mDoAnimateNewData = true;
        }
        if (forceRefresh) {
            fetchGuideData();
        }
    }

    private void fetchGuideData() {
        mDisposables.add(mSchauburgApi.getFullGuide()
                .subscribeOn(Schedulers.io())
                .doOnNext(this::saveGuide)
                .flatMapIterable(Guide::getMovies)
                //work around since you can't pass variable down the stream
                //see https://github.com/square/retrofit/issues/855
                .flatMap(movie -> mTMDbApi.search(movie.getTitle(), 2017)
                        .map(searchResponse -> {
                            setTMDbId(movie, searchResponse.getResults());
                            return searchResponse;
                        }))
                .ignoreElements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {/*ignored*/}, this::showError)
        );

    }

    private void showError(Throwable error) {
        Log.e("GuidePresenter", "error while fetching data", error);
        if (error instanceof SocketTimeoutException
                || error instanceof UnknownHostException
                || error instanceof SocketException) {
            mView.showError("Keine Internetverbindung :(");
        } else {
            mView.showError(error.getClass().getName());
        }
    }

    private void setTMDbId(Movie movie, List<SearchResult> results) {
        if (results.size() == 0) {
            return;
        }
        SearchResult result = results.get(0);
        movie.setTmdbId(result.getId());
        movie.setReleaseDate(result.getReleaseDate());
        movie.setHdPosterUrl("https://image.tmdb.org/t/p/w500" + result.getPosterPath());
        movie.setCoverUrl("https://image.tmdb.org/t/p/w780" + result.getBackdropPath());
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                realm.copyToRealmOrUpdate(movie);
            });
        }
    }


    private void saveGuide(Guide guide) {
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                realm.deleteAll();
                realm.copyToRealm(guide.getScreeningsGroupedByStartTime());
            });
        }
    }
}
