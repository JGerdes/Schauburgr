package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.ScreeningDay;
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

public class GuideView extends Fragment implements GuideContract.View,
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
    private Animation mUpdateAnimation;

    public static GuideView newInstance() {
        Bundle args = new Bundle();

        GuideView fragment = new GuideView();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.home_guide, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_guide);
        ButterKnife.bind(this, view);

        mDayListAdapter = new GuideDaysAdapter();
        mDayList.setAdapter(mDayListAdapter);
        mDayList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        mRefreshLayout.setOnRefreshListener(this);
        int triggerDistance = getContext()
                .getResources().getDimensionPixelSize(R.dimen.swipe_refresh_trigger_distance);
        mRefreshLayout.setDistanceToTriggerSync(triggerDistance);

        mUpdateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);

        bindModel();

        new GuidePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRealm.close();
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
            public void onChange(RealmResults<ScreeningDay> result) {
                if (result.size() > 0) {
                    mStateLayout.setState(StateToggleLayout.STATE_CONTENT);
                } else {
                    mStateLayout.setState(StateToggleLayout.STATE_EMPTY);
                }
                mDayList.startAnimation(mUpdateAnimation);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
        mPresenter = presenter;
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
