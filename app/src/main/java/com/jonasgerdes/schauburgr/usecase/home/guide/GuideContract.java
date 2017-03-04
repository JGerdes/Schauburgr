package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {

    }

    interface Presenter extends BasePresenter {
        void loadProgram();
    }
}