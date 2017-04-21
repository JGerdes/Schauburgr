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
import com.jonasgerdes.schauburgr.util.DateFormatUtil;
import com.jonasgerdes.schauburgr.util.ViewUtils;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class DayHolder extends RecyclerView.ViewHolder {

    private static final DateTimeFormatter FORMAT_TIME = DateTimeFormat.forPattern("HH:mm");

    @BindView(R.id.title)
    TextView mDayTitle;

    @BindView(R.id.screeningList)
    LinearLayout mScreeningList;

    public DayHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(ScreeningDay day) {
        Context context = itemView.getContext();
        mScreeningList.removeAllViews();
        String dayTitle
                = DateFormatUtil.createRelativeDayTitle(context.getResources(), day.getDate());
        mDayTitle.setText(dayTitle);

        boolean isFirst = true;
        boolean isPast = false;
        boolean isToday = day.getDate().isEqual(new LocalDate());
        LocalTime now = new LocalTime();

        for (ScreeningTime screeningTime : day.getTimes()) {
            isPast = isToday && screeningTime.getTime().isBefore(now);
            TextView timeView = createTimeView(context, isFirst);
            timeView.setText(FORMAT_TIME.print(screeningTime.getTime()));
            mScreeningList.addView(timeView);
            makeGrey(timeView, isPast);

            for (Screening screening : screeningTime.getScreenings()) {
                ScreeningView screeningView = new ScreeningView(context);
                screeningView.bindScreening(screening);
                mScreeningList.addView(screeningView);
                makeGrey(screeningView, isPast);
            }
            isFirst = false;
        }
    }

    private void makeGrey(View timeView, boolean doMakeGrey) {
        if (doMakeGrey) {
            timeView.setAlpha(0.5f);
        } else {
            timeView.setAlpha(1f);
        }
    }


    private TextView createTimeView(Context context, boolean isFirst) {
        TextView timeView = new TextView(context);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int horizontalMargin = ViewUtils.dpToPx(context, 16);
        int topMargin = ViewUtils.dpToPx(context, isFirst ? 0 : 24);
        layout.setMargins(horizontalMargin, topMargin, horizontalMargin, 0);

        timeView.setLayoutParams(layout);
        TextViewCompat.setTextAppearance(timeView, R.style.TextAppearance_AppCompat_Caption);
        return timeView;
    }
}
