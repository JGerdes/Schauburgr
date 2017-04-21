package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import io.realm.RealmResults;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showScreeningDays(RealmResults<ScreeningDay> screeningDays, boolean animate);
        void showError(String message);
        void openWebpage(String url);
    }

    interface Presenter extends BasePresenter<GuideContract.View> {
        void onRefreshTriggered();
        void onScreeningSelected(Screening screening);
    }
}