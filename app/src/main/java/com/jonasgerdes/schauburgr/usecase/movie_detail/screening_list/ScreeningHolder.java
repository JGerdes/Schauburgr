package com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.view.MovieAttributeView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.labelHall)
    TextView mHall;

    @BindView(R.id.attibute_list)
    MovieAttributeView mAttributes;

    @BindView(R.id.time)
    TextView mStartTime;


    private static final DateTimeFormatter FORMAT_TIME
            = DateTimeFormat.forPattern("EE, dd.MM - HH:mm");

    public ScreeningHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Screening screening) {
        mStartTime.setText(screening.getStartDate().toString(FORMAT_TIME));
        mAttributes.setAttributes(screening.getMovie().getExtras());
        mHall.setText(String.valueOf(screening.getHall()));

    }
}
