package com.jonasgerdes.schauburgr.usecase.home.guide;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuidePresenter implements GuideContract.Presenter {

    @Inject
    Retrofit mRetrofit;

    private GuideContract.View mView;

    public GuidePresenter(GuideContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void loadProgram() {

    }
}
