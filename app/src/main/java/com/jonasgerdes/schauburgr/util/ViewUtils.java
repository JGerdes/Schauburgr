package com.jonasgerdes.schauburgr.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

/**
 * Static collection of various useful {@link View} related operations
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.03.2017
 */

public class ViewUtils {

    /**
     * Converts given dp to pixel for current device dpi
     *
     * @param context Context to get {@link android.util.DisplayMetrics} from
     * @param dp      Dp to convert to pixel
     * @return Converted pixel value
     */
    public static float dpToPx(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    /**
     * Converts given dp to pixel for current device dpi
     *
     * @param context Context to get {@link android.util.DisplayMetrics} from
     * @param dp      Dp to convert to pixel
     * @return Converted pixel value
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    /**
     * Sets visibility of given view based on given boolean either to {@link View#VISIBLE} or
     * {@link View#GONE}
     *
     * @param view      view to change visibility of
     * @param isVisible true if view should be visible, false otherwise
     */
    public static void setVisible(View view, boolean isVisible) {
        if (view == null) {
            return;
        }
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
