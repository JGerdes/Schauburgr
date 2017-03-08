package com.jonasgerdes.schauburgr.usecase.home.about;

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
 * Created by jonas on 08.03.2017.
 */

public class AboutView extends FrameLayout implements HomeView, AboutContract.View {
    private AboutContract.Presenter mPresenter;


    public AboutView(@NonNull Context context) {
        super(context);
        init();
    }

    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AboutView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_about, this);
        ButterKnife.bind(this);

    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
