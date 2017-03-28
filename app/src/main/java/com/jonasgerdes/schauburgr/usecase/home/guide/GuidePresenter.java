package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.util.Log;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import java.net.SocketTimeoutException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuidePresenter implements GuideContract.Presenter {
    private static final String TAG = "AboutPresenter";

    @Inject
    SchauburgApi mApi;

    private GuideContract.View mView;
    private Call<Guide> mCall;

    public GuidePresenter(GuideContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        if (Realm.getDefaultInstance().where(ScreeningDay.class).count() == 0) {
            loadProgram();
        }
    }

    @Override
    public void loadProgram() {
        mCall = mApi.getFullGuide();
        mCall.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                List<ScreeningDay> guide = response.body().getScreeningsGroupedByStartTime();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(guide);
                realm.commitTransaction();
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                Log.e(TAG, "Failure while feting data", t);
                if (t instanceof SocketTimeoutException) {
                    mView.showError("Keine Internetverbindung :(");
                } else {
                    mView.showError(t.getClass().getName());
                }
            }
        });
    }

    @Override
    public void onStop() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
