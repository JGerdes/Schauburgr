package com.jonasgerdes.schauburgr.usecase.home.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.OpenSourceLicense;
import com.jonasgerdes.schauburgr.usecase.home.about.license_list.LicenseListAdapter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 08.03.2017.
 */

public class AboutView extends Fragment implements AboutContract.View {

    @BindView(R.id.licenseList)
    RecyclerView mLicenseList;

    @BindView(R.id.versionName)
    TextView mVersionName;

    private AboutContract.Presenter mPresenter;
    private LicenseListAdapter mLicenseAdapter;

    public static AboutView newInstance() {
        Bundle args = new Bundle();

        AboutView fragment = new AboutView();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.home_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_about);
        ButterKnife.bind(this, view);
        mLicenseAdapter = new LicenseListAdapter();
        mLicenseList.setAdapter(mLicenseAdapter);
        mLicenseList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        new AboutPresenter().attachView(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mPresenter = presenter;
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
