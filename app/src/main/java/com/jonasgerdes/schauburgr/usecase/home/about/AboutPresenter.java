package com.jonasgerdes.schauburgr.usecase.home.about;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.OpenSourceLicense;

import javax.inject.Inject;

/**
 * Created by jonas on 08.03.2017.
 */

public class AboutPresenter implements AboutContract.Presenter {

    @Inject
    App mApp;

    private AboutContract.View mView;

    public AboutPresenter(AboutContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        mView.setVersionName(mApp.getVersionName());
    }

    @Override
    public void loadLicenses() {
        mView.setLicenses(
                new OpenSourceLicense(
                        R.string.license_support_title,
                        R.string.license_support_body),
                new OpenSourceLicense(
                        R.string.license_butterknife_title,
                        R.string.license_butterknife_body),
                new OpenSourceLicense(
                        R.string.license_dagger_title,
                        R.string.license_dagger_body),
                new OpenSourceLicense(
                        R.string.license_glide_title,
                        R.string.license_glide_body),
                new OpenSourceLicense(
                        R.string.license_gson_title,
                        R.string.license_gson_body),
                new OpenSourceLicense(
                        R.string.license_jodatime_title,
                        R.string.license_jodatime_body),
                new OpenSourceLicense(
                        R.string.license_material_icons_title,
                        R.string.license_material_icons_body),
                new OpenSourceLicense(
                        R.string.license_okhttp_title,
                        R.string.license_okhttp_body),
                new OpenSourceLicense(
                        R.string.license_retrofit_title,
                        R.string.license_retrofit_body)
        );
    }
}
