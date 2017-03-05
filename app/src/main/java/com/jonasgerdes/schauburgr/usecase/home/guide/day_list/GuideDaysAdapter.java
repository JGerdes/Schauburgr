package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.ScreeningDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public class GuideDaysAdapter extends RecyclerView.Adapter<DayHolder> {

    private List<ScreeningDay> mDays = new ArrayList<>();

    @Override
    public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_guide_item_day, parent, false);
        return new DayHolder(view);
    }

    @Override
    public void onBindViewHolder(DayHolder holder, int position) {
        ScreeningDay day = mDays.get(position);
        holder.onBind(day);
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public void setDays(List<ScreeningDay> days) {
        mDays = days;
        notifyDataSetChanged();
    }
}
