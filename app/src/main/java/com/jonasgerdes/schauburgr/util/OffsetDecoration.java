package com.jonasgerdes.schauburgr.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class OffsetDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalOffset;
    private final int mHorizontalOffset;

    public OffsetDecoration(int verticalOffset, int horizontalOffset) {
        mVerticalOffset = verticalOffset;
        mHorizontalOffset = horizontalOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mVerticalOffset;
            outRect.left = mHorizontalOffset;
        }
    }
}
