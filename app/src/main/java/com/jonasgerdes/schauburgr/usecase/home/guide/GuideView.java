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

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

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
    private Realm mRealm;

    public GuideView(@NonNull Context context) {
        super(context);
        init();
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.home_guide, this);
        ButterKnife.bind(this);
        mStateLayout.setState(StateToggleLayout.STATE_EMPTY);
        mDayListAdapter = new GuideDaysAdapter(getContext());
        mDayList.setAdapter(mDayListAdapter);
        mDayList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        mRefreshLayout.setOnRefreshListener(this);
        int triggerDistance = getContext()
                .getResources().getDimensionPixelSize(R.dimen.swipe_refresh_trigger_distance);
        mRefreshLayout.setDistanceToTriggerSync(triggerDistance);

        bindModel();

        new GuidePresenter(this);
    }

    private void bindModel() {
        mRealm = Realm.getDefaultInstance();
        RealmResults<ScreeningDay> days
                = mRealm.where(ScreeningDay.class)
                .greaterThanOrEqualTo("date", new LocalDate().toDate())
                .findAllSorted("date", Sort.ASCENDING);
        mDayListAdapter.setDays(days);
        mStateLayout.setState(StateToggleLayout.STATE_CONTENT);
        days.addChangeListener(new RealmChangeListener<RealmResults<ScreeningDay>>() {
            @Override
            public void onChange(RealmResults<ScreeningDay> element) {
                mStateLayout.setState(StateToggleLayout.STATE_CONTENT);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }

    @Override
    public void showError(String message) {
        mErrorMessageView.setText(message);
        mStateLayout.setState(StateToggleLayout.STATE_ERROR);
        mRefreshLayout.setRefreshing(false);
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
