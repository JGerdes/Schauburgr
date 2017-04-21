package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.model.ScreeningDay;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by jonas on 05.03.2017.
 */

public class GuideDaysAdapter extends RecyclerView.Adapter<DayHolder>
        implements ScreeningSelectedListener{

    private List<ScreeningDay> mDays = new ArrayList<>();
    private ScreeningSelectedListener mListener;

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_guide_item_day, parent, false);
        return new DayHolder(view);
    }

    @Override
    public void onBindViewHolder(DayHolder holder, int position) {
        ScreeningDay day = mDays.get(position);
        holder.onBind(day, this);
    }

    @Override
    public void onViewDetachedFromWindow(DayHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public void setDays(RealmResults<ScreeningDay> days) {
        mDays = days;
        notifyDataSetChanged();
        days.addChangeListener(new RealmChangeListener<RealmResults<ScreeningDay>>() {
            @Override
            public void onChange(RealmResults<ScreeningDay> element) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        if(mListener != null) {
            mListener.onScreeningSelected(screening);
        }
    }

    public void setListener(ScreeningSelectedListener listener) {
        mListener = listener;
    }
}
