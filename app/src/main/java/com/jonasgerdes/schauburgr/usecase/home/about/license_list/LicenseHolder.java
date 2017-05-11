package com.jonasgerdes.schauburgr.usecase.home.about.license_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.OpenSourceLicense;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 08.03.2017.
 */

public class LicenseHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.body)
    TextView mBody;


    public LicenseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(OpenSourceLicense license) {
        mTitle.setText(license.getTitle());
        mBody.setText(license.getBody());
    }
}
