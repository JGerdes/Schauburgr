package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import org.joda.time.LocalDate;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
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
    Realm mRealm;

    private GuideContract.View mView;
    private Call<Guide> mPendingCall;

    public GuidePresenter(GuideContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        showCachedDataOrFetch();
    }

    private void showCachedDataOrFetch() {
        if (Realm.getDefaultInstance().where(ScreeningDay.class).count() == 0) {
            loadProgram();
        } else {
            RealmResults<ScreeningDay> days
                    = mRealm.where(ScreeningDay.class)
                    .greaterThanOrEqualTo("date", new LocalDate().toDate())
                    .findAllSorted("date", Sort.ASCENDING);
            mView.showScreeningDays(days);
        }
    }

    @Override
    public void loadProgram() {
        mPendingCall = mApi.getFullGuide();
        mPendingCall.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                final List<ScreeningDay> guide = response.body().getScreeningsGroupedByStartTime();
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(guide);
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
            }
        });
    }

    @Override
    public void stop() {
        if (mPendingCall != null) {
            mPendingCall.cancel();
        }
    }
}
