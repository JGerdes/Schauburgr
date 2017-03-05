package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Screening;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class ScreeningView extends FrameLayout {

    private static final float ALPHA_ENBALED = 1f;
    private static final float ALPHA_DISABLED = 0.1f;
    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.label3d)
    TextView mLabel3D;

    @BindView(R.id.labelAtmos)
    TextView mLabelAtmos;

    @BindView(R.id.labelHall)
    TextView mLabelHall;

    public ScreeningView(Context context) {
        super(context);
        init();
    }

    public ScreeningView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScreeningView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScreeningView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_guide_item_screening, this);
        ButterKnife.bind(this);
    }

    void bindScreening(Screening screening) {
        mTitle.setText(screening.getMovie().getTitle());
        mLabel3D.setAlpha(screening.getMovie().is3D() ? ALPHA_ENBALED : ALPHA_DISABLED);
        mLabelAtmos.setAlpha(screening.getMovie().isAtmos() ? ALPHA_ENBALED : ALPHA_DISABLED);
        mLabelHall.setText(String.valueOf(screening.getHall()));
    }
}
