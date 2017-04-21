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
 * Layout which holds up to three children. First child should be view to be shown when there is no
 * data ({@link #STATE_EMPTY}), second when there is content ({@link #STATE_CONTENT}) and third when
 * there is an error ({@link #STATE_ERROR}).The default starting state is {link #STATE_EMPTY}.
 * Currently can only be used by inflating, not by creating an instance via its constructor.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 12.03.2017
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

    /**
     * View to be shown when the state is {@link #STATE_EMPTY}
     */
    @Nullable
    private View mEmptyView;

    /**
     * View to be shown when the state is {@link #STATE_CONTENT}
     */
    @Nullable
    private View mContentView;

    /**
     * View to be shown when the state is {@link #STATE_CONTENT}
     */
    @Nullable
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

    /**
     * Set current state and update visible views
     * @param state new state
     */
    public void setState(@State int state) {
        mState = state;
        updateState();
    }

    /**
     * Shows or hides views based on current state and state the views a for
     */
    private void updateState() {
        ViewUtils.setVisible(mEmptyView, mState == STATE_EMPTY);
        ViewUtils.setVisible(mContentView, mState == STATE_CONTENT);
        ViewUtils.setVisible(mErrorView, mState == STATE_ERROR);
    }
}
