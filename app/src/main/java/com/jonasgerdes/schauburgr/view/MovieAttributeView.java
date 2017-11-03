package com.jonasgerdes.schauburgr.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.ExtraMapper;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.util.ViewUtils;

import java.util.List;

/**
 * View which displays attributes (of a movie) in an horizontal list.
 * Generally behaves like a {@link LinearLayout}, which it extends from.
 *
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

    /**
     * Setups view by setting orientation.
     */
    private void init() {
        setOrientation(HORIZONTAL);
    }

    /**
     * Set attributes to show. Creates a {@link TextView} for each attribute, applies style
     * and add it as child.
     *
     * @param attributes attributes titles to show
     */
    public void setAttributes(List<String> attributes) {
        //remove existing extras
        removeAllViews();
        //add new ones
        for (String attributeName : attributes) {
            @StringRes int attribute = ExtraMapper.getExtraString(attributeName);
            if (attribute == ExtraMapper.NONE) {
                return;
            }
            TextView attributeView = new TextView(getContext(), null, 0, R.style.movie_attribute);
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int horizontalMarginDp = ViewUtils.dpToPx(getContext(), 4);
            layout.setMargins(horizontalMarginDp, 0, horizontalMarginDp, 0);
            layout.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            attributeView.setLayoutParams(layout);
            attributeView.setText(attribute);

            if (Movie.EXTRA_LAST_SCREENINGS.equals(attributeName)) {
                attributeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            }
            addView(attributeView);
        }
    }
}
