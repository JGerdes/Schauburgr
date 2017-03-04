package com.jonasgerdes.schauburgr.usecase.home.guide;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuidePresenter implements GuideContract.Presenter {

    private GuideContract.View mView;

    public GuidePresenter(GuideContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void loadProgram() {

    }
}
