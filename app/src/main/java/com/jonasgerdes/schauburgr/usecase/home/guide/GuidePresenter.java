package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.network.SchauburgApi;
import com.jonasgerdes.schauburgr.network.url.UrlProvider;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private Call<Guide> mPendingCall;
    private RealmResults<ScreeningDay> mScreeningDays;
    private boolean mDoAnimateNewData = false;

    private RealmChangeListener<RealmResults<ScreeningDay>>
            mChangeListener = new RealmChangeListener<RealmResults<ScreeningDay>>() {
        @Override
        public void onChange(RealmResults<ScreeningDay> screeningDays) {
            mView.showScreeningDays(screeningDays, mDoAnimateNewData);
            mDoAnimateNewData = false;
        }
    };

    @Override
    public void attachView(GuideContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        loadGuide();
    }

    @Override
    public void detachView() {
        if (mPendingCall != null) {
            mPendingCall.cancel();
        }
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
        mPendingCall = mApi.getFullGuide();
        mPendingCall.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                final List<ScreeningDay> guide = response.body().getScreeningsGroupedByStartTime();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                        realm.copyToRealm(guide);
                    }
                });
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                if (t instanceof SocketTimeoutException
                        || t instanceof UnknownHostException
                        || t instanceof SocketException) {
                    mView.showError("Keine Internetverbindung :(");
                } else {
                    mView.showError(t.getClass().getName());
                }
                mPendingCall = null;
            }
        });
    }
}
