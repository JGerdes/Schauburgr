package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showError(String message);

    }

    interface Presenter extends BasePresenter {
        void loadProgram();
        void stop();
    }
}