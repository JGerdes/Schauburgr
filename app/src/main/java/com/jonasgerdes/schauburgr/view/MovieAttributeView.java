package com.jonasgerdes.schauburgr.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.util.ViewUtils;

import java.util.List;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 31.03.2017
 */

public class MovieAttributeView extends LinearLayout {
    public MovieAttributeView(Context context) {
        super(context);
        init();
    }

    public MovieAttributeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovieAttributeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MovieAttributeView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    public void setAttributes(List<String> attributes) {
        for (String attribute : attributes) {
            TextView attributeView = new TextView(getContext(), null, 0, R.style.movie_attribute);
            LinearLayoutCompat.LayoutParams layout = new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int horizontalMarginDp = ViewUtils.dpToPx(getContext(), 4);
            layout.setMargins(horizontalMarginDp, 0, horizontalMarginDp, 0);
            attributeView.setLayoutParams(layout);
            attributeView.setText(attribute);
            addView(attributeView);
        }
    }
}