package com.jonasgerdes.schauburgr.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.jonasgerdes.schauburgr.util.ViewUtils;

/**
 * Created by jonas on 12.03.2017.
 */

public class StateToggleLayout extends FrameLayout {

    public static final int STATE_EMPTY = 0;
    public static final int STATE_CONTENT = 1;
    public static final int STATE_ERROR = -1;

    @IntDef({
            STATE_EMPTY,
            STATE_CONTENT,
            STATE_ERROR
    })
    public @interface State {
    }

    @State
    private int mState = STATE_EMPTY;

    private View mEmptyView;
    private View mContentView;
    private View mErrorView;

    public StateToggleLayout(@NonNull Context context) {
        super(context);
    }

    public StateToggleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateToggleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StateToggleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bindViews();
        setState(STATE_EMPTY);

    }

    private void bindViews() {
        if (getChildCount() > 0) {
            mEmptyView = getChildAt(0);
        }

        if (getChildCount() > 1) {
            mContentView = getChildAt(1);
        }

        if (getChildCount() > 2) {
            mErrorView = getChildAt(2);
        }
    }

    public void setState(@State int state) {
        mState = state;
        updateState();
    }

    private void updateState() {
        ViewUtils.setVisible(mEmptyView, mState == STATE_EMPTY);
        ViewUtils.setVisible(mContentView, mState == STATE_CONTENT);
        ViewUtils.setVisible(mErrorView, mState == STATE_ERROR);
    }
}
