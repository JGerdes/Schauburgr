package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import android.content.Context;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class DayHolder extends RecyclerView.ViewHolder {

    private static final DateFormat FORMAT_DAY = new SimpleDateFormat("dd.MM", Locale.GERMAN);
    private static final DateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm", Locale.GERMAN);

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
        mDayTile.setText(FORMAT_DAY.format(day.getDate().getTime()));

        for (ScreeningTime screeningTime : day.getTimes()) {
            TextView timeView = createTimeView(context);
            timeView.setText(FORMAT_TIME.format(screeningTime.getTime().getTime()));
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
        timeView.setTextAppearance(R.style.TextAppearance_AppCompat_Caption);
        return timeView;
    }
}
