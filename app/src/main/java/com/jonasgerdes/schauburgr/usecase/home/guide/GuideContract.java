package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
import com.jonasgerdes.schauburgr.model.schauburg.entity.ScreeningDay;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import io.realm.RealmResults;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showScreeningDays(RealmResults<ScreeningDay> screeningDays, boolean animate);
        void showIsLoading(boolean show);
        void showError(String message);
        void showError(int messageResource);
        void hideError();
        void openWebpage(String url);
    }

    interface Presenter extends BasePresenter<GuideContract.View> {
        void loadGuide(boolean forceRefresh);
        void onRefreshTriggered();
        void onScreeningSelected(Screening screening);
        void onCinemaChanged();
    }
}