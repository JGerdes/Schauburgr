package com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Screening;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class ScreeningListAdapter extends RecyclerView.Adapter<ScreeningHolder> {
    private List<Screening> mScreenings = new ArrayList<>();

    @Override
    public ScreeningHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_detail_item_screening, parent, false);
        return new ScreeningHolder(view);
    }

    @Override
    public void onBindViewHolder(ScreeningHolder holder, int position) {
        Screening screening = mScreenings.get(position);
        holder.onBind(screening);
    }

    @Override
    public int getItemCount() {
        return mScreenings.size();
    }

    public void setScreenings(RealmResults<Screening> screenings) {
        mScreenings = screenings;
        notifyDataSetChanged();
        screenings.addChangeListener(new RealmChangeListener<RealmResults<Screening>>() {
            @Override
            public void onChange(RealmResults<Screening> element) {
                notifyDataSetChanged();
            }
        });
    }
}
