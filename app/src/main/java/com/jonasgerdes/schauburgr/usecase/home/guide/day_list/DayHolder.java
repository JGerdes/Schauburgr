package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.model.ScreeningTime;
import com.jonasgerdes.schauburgr.util.ViewUtils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class DayHolder extends RecyclerView.ViewHolder {

    private static final DateTimeFormatter FORMAT_DAY = DateTimeFormat.forPattern("dd.MM");
    private static final DateTimeFormatter FORMAT_TIME = DateTimeFormat.forPattern("HH:mm");

    @BindView(R.id.title)
    TextView mDayTile;

    @BindView(R.id.screeningList)
    LinearLayout mScreeningList;

    public DayHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(ScreeningDay day) {
        Context context = itemView.getContext();
        mScreeningList.removeAllViews();
        mDayTile.setText(FORMAT_DAY.print(day.getDate()));

        for (ScreeningTime screeningTime : day.getTimes()) {
            TextView timeView = createTimeView(context);
            timeView.setText(FORMAT_TIME.print(screeningTime.getTime()));
            mScreeningList.addView(timeView);

            for (Screening screening : screeningTime.getScreenings()) {
                ScreeningView screeningView = new ScreeningView(context);
                screeningView.bindScreening(screening);
                mScreeningList.addView(screeningView);
            }
        }
    }

    private TextView createTimeView(Context context) {
        TextView timeView = new TextView(context);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int horizontalMargin = ViewUtils.dpToPx(context, 16);
        int topMargin = ViewUtils.dpToPx(context, 24);
        layout.setMargins(horizontalMargin, topMargin, horizontalMargin, 0);

        timeView.setLayoutParams(layout);
        TextViewCompat.setTextAppearance(timeView, R.style.TextAppearance_AppCompat_Caption);
        return timeView;
    }
}
