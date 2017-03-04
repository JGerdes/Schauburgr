package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.usecase.home.HomeView;

import butterknife.ButterKnife;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuideView extends FrameLayout implements HomeView, GuideContract.View {
    private GuideContract.Presenter mPresenter;

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

}
