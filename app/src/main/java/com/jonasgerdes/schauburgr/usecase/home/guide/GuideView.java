package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
import com.jonasgerdes.schauburgr.usecase.home.HomeView;
import com.jonasgerdes.schauburgr.usecase.home.guide.day_list.GuideDaysAdapter;
import com.jonasgerdes.schauburgr.view.StateToggleLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuideView extends FrameLayout implements HomeView, GuideContract.View,
        SwipeRefreshLayout.OnRefreshListener {
    private GuideContract.Presenter mPresenter;

    @BindView(R.id.stateToggleLayout)
    StateToggleLayout mStateLayout;

    @BindView(R.id.day_list)
    RecyclerView mDayList;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.errorMessage)
    TextView mErrorMessageView;

    private GuideDaysAdapter mDayListAdapter;

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
        mStateLayout.setState(StateToggleLayout.STATE_EMPTY);
        mDayListAdapter = new GuideDaysAdapter();
        mDayList.setAdapter(mDayListAdapter);
        mDayList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        mRefreshLayout.setOnRefreshListener(this);

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
    public void showGuide(List<ScreeningDay> days) {
        mDayListAdapter.setDays(days);
        mStateLayout.setState(StateToggleLayout.STATE_CONTENT);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        mErrorMessageView.setText(message);
        mStateLayout.setState(StateToggleLayout.STATE_ERROR);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.loadProgram();
        } else {
            mErrorMessageView.setText(R.string.guide_state_hint_error_default);
            mStateLayout.setState(StateToggleLayout.STATE_ERROR);
        }
    }
}
