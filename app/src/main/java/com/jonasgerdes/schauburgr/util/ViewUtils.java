package com.jonasgerdes.schauburgr.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

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

    /**
     * Sets given resource of AnimatedVectorDrawable to given ImageView and starts the animation.
     * Uses AnimatedVectorDrawableCompat to work with Lollipop
     * @param view ImageView to show animation in
     * @param animatedDrawable AnimatedVectorDrawable to show
     */
    public static void loadAnimationAndStart(ImageView view, @DrawableRes int animatedDrawable) {
        AnimatedVectorDrawableCompat animatedVector
                = AnimatedVectorDrawableCompat.create(
                view.getContext(),
                animatedDrawable
        );
        view.setImageDrawable(animatedVector);
        animatedVector.start();
    }
}
