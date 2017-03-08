package com.jonasgerdes.schauburgr.usecase.home.about;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.OpenSourceLicense;

/**
 * Created by jonas on 08.03.2017.
 */

public class AboutPresenter implements AboutContract.Presenter {

    private AboutContract.View mView;

    public AboutPresenter(AboutContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadLicenses() {
        mView.setLicenses(
                new OpenSourceLicense(
                        R.string.license_support_title,
                        R.string.license_support_body)
        );
    }
}
