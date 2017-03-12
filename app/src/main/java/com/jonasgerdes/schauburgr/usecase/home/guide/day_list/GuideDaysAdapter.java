package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.ScreeningDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 05.03.2017.
 */

public class GuideDaysAdapter extends RecyclerView.Adapter<DayHolder> {

    private final Animation mAppearAnimation;
    private List<ScreeningDay> mDays = new ArrayList<>();
    private int mLastPositionAnimated = -1;

    public GuideDaysAdapter(Context context) {
        mAppearAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
    }

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
        startAnimation(holder.itemView, position);
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

    public void setDays(List<ScreeningDay> days) {
        mDays = days;
        mLastPositionAnimated = -1;
        notifyDataSetChanged();
    }

    private void startAnimation(View toAnimate, int position) {
        if (position > mLastPositionAnimated) {
            toAnimate.startAnimation(mAppearAnimation);
            mLastPositionAnimated = position;
        }
    }
}
