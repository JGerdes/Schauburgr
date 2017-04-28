package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.util.Log;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

import org.joda.time.DateTime;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuidePresenter implements GuideContract.Presenter {

    @Inject
    MovieRepository mMovieRepository;

    @Inject
    UrlProvider mUrlProvider;


    private GuideContract.View mView;
    private CompositeDisposable mDisposables;
    private boolean mDoAnimateNewData = false;

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
    }

    @Override
    public void onRefreshTriggered() {
        //show user new data with animation
        mDoAnimateNewData = true;
        mMovieRepository.loadMovieData();
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
        mDisposables.add(mMovieRepository.getScreeningDays()
                .subscribe(screeningDays -> {
                    //show animation on first load
                    if (screeningDays.size() == 0) {
                        mDoAnimateNewData = true;
                    }
                    mView.showScreeningDays(screeningDays, mDoAnimateNewData);
                    mDoAnimateNewData = false;
                })
        );
        if (forceRefresh) {
            mMovieRepository.loadMovieData();
        }
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

}
