package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.NetworkState;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
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

        mDisposables.add(mMovieRepository.getNetworkState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleNetworkState)
        );
    }

    private void handleNetworkState(NetworkState state) {
        switch (state.getState()) {
            case NetworkState.STATE_DEFAULT:
                mView.showIsLoading(false);
                mView.hideError();
                return;
            case NetworkState.STATE_LOADING:
                mView.showIsLoading(true);
                mView.hideError();
                return;
            case NetworkState.STATE_ERROR:
                mView.showIsLoading(false);
                if (state.getMessageResource() != NetworkState.NO_MESSAGE) {
                    mView.showError(state.getMessage());
                } else {
                    mView.showError(state.getMessage());
                }
        }
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

}
