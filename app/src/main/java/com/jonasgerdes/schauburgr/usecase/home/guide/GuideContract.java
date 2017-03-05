package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showGuide(Guide guide);

    }

    interface Presenter extends BasePresenter {
        void loadProgram();
    }
}