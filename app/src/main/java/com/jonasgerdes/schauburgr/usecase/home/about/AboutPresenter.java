package com.jonasgerdes.schauburgr.usecase.home.about;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.CinemaHost;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgUrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.OpenSourceLicense;

import javax.inject.Inject;

/**
 * Created by jonas on 08.03.2017.
 */

public class AboutPresenter implements AboutContract.Presenter {

    @Inject
    App mApp;

    @Inject
    SchauburgUrlProvider mSchauburgUrlProvider;

    @Inject
    MovieRepository mMovieRepository;

    private AboutContract.View mView;

    @Override
    public void attachView(AboutContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        mView.setVersionName(mApp.getVersionName());
        loadLicenses();
    }

    @Override
    public void detachView() {

    }

    private void loadLicenses() {
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
                        R.string.license_dart_title,
                        R.string.license_dart_body),
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
                        R.string.license_realm_core_title,
                        R.string.license_realm_core_body),
                new OpenSourceLicense(
                        R.string.license_realm_java_title,
                        R.string.license_realm_java_body),
                new OpenSourceLicense(
                        R.string.license_retrofit_title,
                        R.string.license_retrofit_body),
                new OpenSourceLicense(
                        R.string.license_rxandroid_title,
                        R.string.license_rxandroid_body),
                new OpenSourceLicense(
                        R.string.license_rxjava_title,
                        R.string.license_rxjava_body)
                );
    }

    @Override
    public void setCinemaHost(CinemaHost host) {
        mSchauburgUrlProvider.setHost(host);
        mView.navigateToGuide();
        mMovieRepository.loadMovieData();
    }
}
