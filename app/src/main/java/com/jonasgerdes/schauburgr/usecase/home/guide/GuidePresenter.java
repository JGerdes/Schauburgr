package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    }


    @Override
    public void loadProgram() {
        mCall = mApi.getFullGuide();
        mCall.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                List<ScreeningDay> allDays = response.body().getScreeningsGroupedByStartTime();
                List<ScreeningDay> daysToShow = new ArrayList<>();
                //don't show past days
                LocalDate today = new LocalDate();
                for (ScreeningDay day : allDays) {
                    if (!day.getDate().isBefore(today)) {
                        daysToShow.add(day);
                    }
                }
                mView.showGuide(daysToShow);
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                mView.showError(t.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        if(mCall != null) {
            mCall.cancel();
        }
    }
}
