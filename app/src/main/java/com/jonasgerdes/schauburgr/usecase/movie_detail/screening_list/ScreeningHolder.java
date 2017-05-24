package com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
import com.jonasgerdes.schauburgr.util.DateFormatUtil;
import com.jonasgerdes.schauburgr.view.MovieAttributeView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningHolder extends RecyclerView.ViewHolder {

    @Inject
    Resources mResources;

    @BindView(R.id.labelHall)
    TextView mHall;

    @BindView(R.id.attibute_list)
    MovieAttributeView mAttributes;

    @BindView(R.id.date)
    TextView mStartDate;

    @BindView(R.id.time)
    TextView mStartTime;


    private final DateTimeFormatter FORMAT_DATE;
    private final DateTimeFormatter FORMAT_TIME;

    public ScreeningHolder(View itemView) {
        super(itemView);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this, itemView);
        FORMAT_DATE = DateTimeFormat.forPattern(
                mResources.getString(R.string.screening_format_date)
        );
        FORMAT_TIME = DateTimeFormat.forPattern(
                mResources.getString(R.string.screenings_format_time)
        );
    }

    public void onBind(Screening screening, boolean isFirstForDate) {
        mStartTime.setText(screening.getStartDate().toString(FORMAT_TIME));
        mAttributes.setAttributes(screening.getMovie().getExtras());
        mHall.setText(String.valueOf(screening.getHall()));

        //show date only if first for the date it's shown on
        //also add some extra margin on first
        if (isFirstForDate) {
            setTopMargin(mResources.getDimensionPixelOffset(
                    R.dimen.movie_detail_item_screening_date_margin
            ));
            String dateString = DateFormatUtil.createRelativeDayTitle(
                    mResources,
                    screening.getStartDate().toLocalDate(),
                    FORMAT_DATE
            );
            mStartDate.setVisibility(View.VISIBLE);
            mStartDate.setText(dateString);
        } else {
            setTopMargin(0);
            mStartDate.setVisibility(View.INVISIBLE);
        }
    }

    private void setTopMargin(int marginTop) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        params.setMargins(0, marginTop, 0, 0);
        itemView.setLayoutParams(params);
    }
}
