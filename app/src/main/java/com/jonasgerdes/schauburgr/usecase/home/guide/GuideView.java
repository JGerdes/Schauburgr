package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.usecase.home.HomeView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuideView extends FrameLayout implements HomeView, GuideContract.View {
    private GuideContract.Presenter mPresenter;

    @BindView(R.id.content)
    TextView mContentView;

    public GuideView(@NonNull Context context) {
        super(context);
        init();
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_guide, this);
        ButterKnife.bind(this);
        new GuidePresenter(this);

    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
        mPresenter.loadProgram();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void showScreenings(List<Screening> screenings) {
       /* Collections.sort(screenings, new Comparator<Screening>() {
            @Override
            public int compare(Screening o1, Screening o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });*/
        String text = "";

        Calendar lastDate = null;
        for (Screening screening : screenings) {

            DateFormat time = new SimpleDateFormat("HH:mm", Locale.GERMAN);
            DateFormat date = new SimpleDateFormat("dd.MM", Locale.GERMAN);

            if (lastDate == null
                    || lastDate.get(Calendar.YEAR) != screening.getStartDate().get(Calendar.YEAR)
                    || lastDate.get(Calendar.MONTH) != screening.getStartDate().get(Calendar.MONTH)
                    || lastDate.get(Calendar.DAY_OF_MONTH) != screening.getStartDate().get(Calendar.DAY_OF_MONTH)
                    ) {
                if(lastDate != null) {
                    text += "\n\n\n";
                }
                lastDate = screening.getStartDate();
                text += date.format(lastDate.getTime());
            }

            text += "\n";
            text += time.format(screening.getStartDate().getTime());
            text += " [" + screening.getHall() + "] ";
            text += screening.getMovie().getTitle() + " (" + screening.getMovie().getDuration() + ")";
        }

        mContentView.setText(text);
    }
}
