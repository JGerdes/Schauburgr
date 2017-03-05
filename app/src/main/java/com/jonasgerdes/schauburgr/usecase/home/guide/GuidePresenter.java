package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.util.Log;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuidePresenter implements GuideContract.Presenter {
    private static final String TAG = "GuidePresenter";

    @Inject
    SchauburgApi mApi;

    private GuideContract.View mView;

    public GuidePresenter(GuideContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void loadProgram() {
        mApi.getFullGuide().enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                mView.showGuide(response.body());
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
