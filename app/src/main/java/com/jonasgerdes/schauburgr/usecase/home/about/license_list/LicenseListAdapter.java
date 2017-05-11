package com.jonasgerdes.schauburgr.usecase.home.about.license_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.OpenSourceLicense;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 08.03.2017.
 */

public class LicenseListAdapter extends RecyclerView.Adapter<LicenseHolder> {

    List<OpenSourceLicense> mLicenseList = new ArrayList<>();

    @Override
    public LicenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_item_license, parent, false);
        return new LicenseHolder(view);
    }

    @Override
    public void onBindViewHolder(LicenseHolder holder, int position) {
        holder.onBind(mLicenseList.get(position));
    }

    @Override
    public int getItemCount() {
        return mLicenseList.size();
    }

    public void setLicenseList(List<OpenSourceLicense> licenseList) {
        mLicenseList = licenseList;
        notifyDataSetChanged();
    }
}
