package com.jonasgerdes.schauburgr.usecase.home.about;

/**
 * Created by jonas on 08.03.2017.
 */

public class AboutPresenter implements AboutContract.Presenter {

    private AboutContract.View mView;

    public AboutPresenter(AboutContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

}
