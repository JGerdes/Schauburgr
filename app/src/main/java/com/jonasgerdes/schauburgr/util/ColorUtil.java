package com.jonasgerdes.schauburgr.util;

import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;

/**
 * Static collection of various useful color operations
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.05.2017
 */

public class ColorUtil {

    /**
     * Modifies color so that its brightness/lightness value is at max the given brightness
     * @param color color to modify
     * @param maxBrightness max brightness of color (0-1)
     * @return new modified color
     */
    public static  @ColorInt int maxBrightness(@ColorInt int color, float maxBrightness) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        hsl[2] = Math.min(hsl[2], maxBrightness);
        return ColorUtils.HSLToColor(hsl);
    }
}
