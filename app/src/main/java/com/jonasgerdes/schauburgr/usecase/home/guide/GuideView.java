package com.jonasgerdes.schauburgr.usecase.home.guide;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
import com.jonasgerdes.schauburgr.model.schauburg.entity.ScreeningDay;
import com.jonasgerdes.schauburgr.usecase.home.guide.day_list.GuideDaysAdapter;
import com.jonasgerdes.schauburgr.usecase.home.guide.day_list.ScreeningSelectedListener;
import com.jonasgerdes.schauburgr.util.ChromeCustomTabWrapper;
import com.jonasgerdes.schauburgr.view.StateToggleLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by jonas on 04.03.2017.
 */

public class GuideView extends Fragment implements GuideContract.View,
        SwipeRefreshLayout.OnRefreshListener, ScreeningSelectedListener {

    /**
     * Key for arguments providing information whether first forces refresh was done
     */
    private static final String ARGUMENT_FIRST_TIME_LOADED = "ARGUMENT_FIRST_TIME_LOADED";

    private GuideContract.Presenter mPresenter;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.stateToggleLayout)
    StateToggleLayout mStateLayout;

    @BindView(R.id.day_list)
    RecyclerView mDayList;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @Inject
    Resources mResources;

    @Inject
    ChromeCustomTabWrapper mChromeTab;

    private GuideDaysAdapter mDayListAdapter;
    private Animation mUpdateAnimation;
    private Snackbar mSnackbar;

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
        App.getAppComponent().inject(this);

        mDayListAdapter = new GuideDaysAdapter();
        mDayListAdapter.setListener(this);
        mDayList.setAdapter(mDayListAdapter);
        mDayList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        mRefreshLayout.setOnRefreshListener(this);
        int triggerDistance = getContext()
                .getResources().getDimensionPixelSize(R.dimen.swipe_refresh_trigger_distance);
        mRefreshLayout.setDistanceToTriggerSync(triggerDistance);

        mUpdateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);

        new GuidePresenter().attachView(this);

        mChromeTab.warmup();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        mRefreshLayout.setOnRefreshListener(null);
        mDayList.clearAnimation();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {
        mPresenter = presenter;
        boolean forceRefresh = !getArguments().getBoolean(ARGUMENT_FIRST_TIME_LOADED, false);
        presenter.loadGuide(forceRefresh);
    }

    @Override
    public void showScreeningDays(RealmResults<ScreeningDay> screeningDays, boolean animate) {
        mDayListAdapter.setDays(screeningDays);
        if (screeningDays.size() == 0) {
            mStateLayout.setState(StateToggleLayout.STATE_EMPTY);
        } else {
            mStateLayout.setState(StateToggleLayout.STATE_CONTENT);
        }
        mRefreshLayout.setRefreshing(false);
        hideError();
        if (animate) {
            mDayList.startAnimation(mUpdateAnimation);
        }
        //prevent another forced refresh
        getArguments().putBoolean(ARGUMENT_FIRST_TIME_LOADED, true);
    }

    @Override
    public void showError(String message) {
        mRefreshLayout.setRefreshing(false);
        mSnackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_action_refresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRefreshLayout.setRefreshing(true);
                        onRefresh();
                    }
                });
        mSnackbar.show();
    }

    @Override
    public void openWebpage(String url) {
        mChromeTab.open(getContext(), url);
    }

    @Override
    public void onRefresh() {
        hideError();
        mPresenter.onRefreshTriggered();
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        mPresenter.onScreeningSelected(screening);
    }

    private void hideError() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }
}
