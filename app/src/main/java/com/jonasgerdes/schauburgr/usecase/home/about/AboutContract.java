package com.jonasgerdes.schauburgr.usecase.home.about;

import com.jonasgerdes.schauburgr.model.schauburg.SchauburgUrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.OpenSourceLicense;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

/**
 * Created by jonas on 08.03.2017.
 */

public interface AboutContract {
    interface View extends BaseView<AboutContract.Presenter> {
        void setLicenses(OpenSourceLicense... licenses);
        void setVersionName(String versionName);
        void navigateToGuide();

    }

    interface Presenter extends BasePresenter<AboutContract.View> {
        void setCinemaHost(@SchauburgUrlProvider.CinemaHost String host);
    }
}