package com.jonasgerdes.schauburgr.usecase.home.about;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.OpenSourceLicense;
import com.jonasgerdes.schauburgr.usecase.home.HomeView;
import com.jonasgerdes.schauburgr.usecase.home.about.license_list.LicenseListApdater;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 08.03.2017.
 */

public class AboutView extends FrameLayout implements HomeView, AboutContract.View {

    @BindView(R.id.licenseList)
    RecyclerView mLicenseList;

    @BindView(R.id.versionName)
    TextView mVersionName;

    private AboutContract.Presenter mPresenter;
    private LicenseListApdater mLicenseAdapter;


    public AboutView(@NonNull Context context) {
        super(context);
        init();
    }

    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_about, this);
        ButterKnife.bind(this);
        new AboutPresenter(this);
        mLicenseAdapter = new LicenseListApdater();
        mLicenseList.setAdapter(mLicenseAdapter);
        mLicenseList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        mPresenter.loadLicenses();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setLicenses(OpenSourceLicense... licenses) {
        mLicenseAdapter.setLicenseList(Arrays.asList(licenses));
    }

    @Override
    public void setVersionName(String versionName) {
        mVersionName.setText("v" + versionName);
    }
}
