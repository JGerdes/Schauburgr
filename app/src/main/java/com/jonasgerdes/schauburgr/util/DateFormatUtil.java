package com.jonasgerdes.schauburgr.util;

import android.content.res.Resources;

import com.jonasgerdes.schauburgr.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 13.04.2017
 */

public class DateFormatUtil {

    private static final DateTimeFormatter DEFAULT_FORMAT_DAY
            = DateTimeFormat.forPattern("EEEE, dd.MM.YYYY");

    public static String createRelativeDayTitle(Resources resources, LocalDate date) {
        return createRelativeDayTitle(resources, date, DEFAULT_FORMAT_DAY);
    }

    public static String createRelativeDayTitle(Resources resources, LocalDate date,
                                                 DateTimeFormatter formatter) {
        LocalDate today = new LocalDate();
        if (date.isEqual(today)) {
            return resources.getString(R.string.day_title_today);
        }
        if (date.isEqual(today.plusDays(1))) {
            return resources.getString(R.string.day_title_tomorrow);
        }
        if (Days.daysBetween(today, date).getDays() <= 7) {
            return date.dayOfWeek().getAsText();
        }

        return formatter.print(date);
    }
}
