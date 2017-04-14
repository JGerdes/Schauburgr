package com.jonasgerdes.schauburgr.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 14.04.2017
 */
public class SwipeBackLayout extends FrameLayout {

    public static final float DRAG_SENSITIVITY = 1 / 4f;
    public static final float BACK_ACTION_THRESHOLD = 1 / 4f;

    public interface SwipeListener {

        void onFullSwipeBack();

        void onSwipe(float progress);

    }

    private ViewDragHelper mDragHelper;
    private SwipeListener mListener;

    public SwipeBackLayout(Context context) {
        super(context, null);
        init();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, DRAG_SENSITIVITY, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.max(0, left);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (mListener != null) {
                    mListener.onSwipe(top / (float) getHeight());
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild.getLeft() > getWidth() * BACK_ACTION_THRESHOLD) {
                    if (mListener != null) {
                        mListener.onFullSwipeBack();
                    }
                } else {
                    mDragHelper.settleCapturedViewAt(0, 0);
                    invalidate();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setSwipeListener(SwipeListener listener) {
        mListener = listener;
    }
}