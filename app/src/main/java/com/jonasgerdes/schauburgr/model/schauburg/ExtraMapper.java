package com.jonasgerdes.schauburgr.model.schauburg;

import android.support.annotation.StringRes;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapping between extra constants from {@link Movie} which are saved in the database
 * to displayable string resources.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.05.2017
 */

public class ExtraMapper {

    public static final int NONE = -1;

    private static final Map<String, Integer> sExtraMap = new HashMap<>();

    static {
        sExtraMap.put(Movie.EXTRA_3D, R.string.movie_extra_3d);
        sExtraMap.put(Movie.EXTRA_ATMOS, R.string.movie_extra_atmos);
        sExtraMap.put(Movie.EXTRA_TIP, R.string.movie_extra_tip);
        sExtraMap.put(Movie.EXTRA_OT, R.string.movie_extra_ot);
        sExtraMap.put(Movie.EXTRA_REEL, R.string.movie_extra_reel);
        sExtraMap.put(Movie.EXTRA_PREVIEW, R.string.movie_extra_preview);
        sExtraMap.put(Movie.EXTRA_LAST_SCREENINGS, R.string.movie_extra_last_screening);
    }

    /**
     * Get string resource which can be displayed in UI for given extra constant
     *
     * @param extra Extra to get string resource for
     * @return string resource for given extra or {@link #NONE} if none exists
     */
    public static @StringRes int getExtraString(String extra) {
        if (sExtraMap.containsKey(extra)) {
            return sExtraMap.get(extra);
        }
        return NONE;
    }
}
