package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import io.realm.RealmResults;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showScreeningDays(RealmResults<ScreeningDay> screeningDays);
        void showError(String message);

    }

    interface Presenter extends BasePresenter {
        void attachView(GuideContract.View view);
        void detachView();
        void loadProgram();
    }
}