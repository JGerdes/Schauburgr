package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.network.SchauburgApi;
import com.jonasgerdes.schauburgr.network.url.UrlProvider;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
    SchauburgApi mApi;

    @Inject
    UrlProvider mUrlProvider;

    @Inject
    Realm mRealm;

    private GuideContract.View mView;
    private CompositeDisposable mDisposables;
    private RealmResults<ScreeningDay> mScreeningDays;
    private boolean mDoAnimateNewData = false;

    private RealmChangeListener<RealmResults<ScreeningDay>> mChangeListener =  screeningDays -> {
            mView.showScreeningDays(screeningDays, mDoAnimateNewData);
            mDoAnimateNewData = false;
    };

    @Override
    public void attachView(GuideContract.View view) {
        App.getAppComponent().inject(this);
        mDisposables = new CompositeDisposable();
        mView = view;

        mView.setPresenter(this);
        loadGuide();
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

    private void loadGuide() {
        mScreeningDays = mRealm.where(ScreeningDay.class)
                .greaterThanOrEqualTo("date", new LocalDate().toDate())
                .findAllSorted("date", Sort.ASCENDING);
        mChangeListener.onChange(mScreeningDays);
        mScreeningDays.addChangeListener(mChangeListener);

        if (Realm.getDefaultInstance().where(ScreeningDay.class).count() == 0) {
            //show animation on first load
            mDoAnimateNewData = true;
        }
        fetchGuideData();
    }

    private void fetchGuideData() {
        mDisposables.add(mApi.getFullGuide()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onN
                .subscribe((guide) -> {
                    try(Realm r = Realm.getDefaultInstance()) {
                        r.executeTransaction((realm) -> {
                            realm.deleteAll();
                            realm.copyToRealm(guide.getScreeningsGroupedByStartTime());
                        });
                    }
                }, (error) -> {
                    if (error instanceof SocketTimeoutException
                            || error instanceof UnknownHostException
                            || error instanceof SocketException) {
                        mView.showError("Keine Internetverbindung :(");
                    } else {
                        mView.showError(error.getClass().getName());
                    }
                })
        );

    }
}
