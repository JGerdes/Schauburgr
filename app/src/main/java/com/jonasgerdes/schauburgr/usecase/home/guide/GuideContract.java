package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import java.util.List;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showGuide(List<ScreeningDay> days);
        void showError(String message);

    }

    interface Presenter extends BasePresenter {
        void loadProgram();
    }
}