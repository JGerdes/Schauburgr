package com.jonasgerdes.schauburgr.util;

import android.content.res.Resources;

import com.jonasgerdes.schauburgr.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Helper for easy date formatting
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 13.04.2017
 */

public class DateFormatUtil {

    private static final DateTimeFormatter DEFAULT_FORMAT_DAY
            = DateTimeFormat.forPattern("EEEE, dd.MM.YYYY");


    /**
     * @see DateFormatUtil#createRelativeDayTitle(Resources, LocalDate, DateTimeFormatter
     *
     * @param resources Resources to pull relative names from string resources
     * @param date      Date to format
     * @return String of formatted Date
     */
    public static String createRelativeDayTitle(Resources resources, LocalDate date) {
        return createRelativeDayTitle(resources, date, DEFAULT_FORMAT_DAY);
    }

    /**
     * Creates a title for given date relative to current date. If date is more than 7 days into the
     * future, given {@link DateTimeFormatter} is used to format it.
     *
     * @param resources Resources to pull relative names from string resources
     * @param date      Date to format
     * @param formatter Formatter to format date if its more than 7 days into the future
     * @return String of formatted Date
     */
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
