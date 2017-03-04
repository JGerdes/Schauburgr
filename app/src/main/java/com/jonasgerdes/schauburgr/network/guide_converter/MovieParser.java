package com.jonasgerdes.schauburgr.network.guide_converter;

import com.jonasgerdes.schauburgr.model.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonas on 04.03.2017.
 */

public class MovieParser {
    private static final String REGEX_PREFIX = "new movie\\(";
    private static final String REGEX_ID = "'(\\w\\w\\d\\d?)'";
    private static final String REGEX_TITLE = "'(.*?)'";
    private static final String REGEX_RELEASE_DATE = "'(\\d{10})'";
    private static final String REGEX_DURATION = "'(\\d{2,3})'";
    private static final String REGEX_CONTENT_RATING = "'(\\d\\d?)'";
    private static final String REGEX_DESCRIPTION = "'((?>\\s|\\S)*?)'";
    private static final String REGEX_IS_3D = "'(1?)'";
    private static final String REGEX_STUB = "'.*?'";

    private static final String REGEX_MOVIE = ""
            + REGEX_PREFIX
            + REGEX_ID + ','
            + REGEX_TITLE + ','
            + REGEX_RELEASE_DATE + ','
            + REGEX_DURATION + ','
            + REGEX_CONTENT_RATING + ','
            + REGEX_DESCRIPTION + ','
            + REGEX_STUB + ','
            + REGEX_STUB + ','
            + REGEX_IS_3D;

    private static final java.lang.String PATTERN_RELASE_DATE = "YYMMddHHmm";
    private static final DateFormat FORMAT_RELASE_DATE
            = new SimpleDateFormat(PATTERN_RELASE_DATE, Locale.GERMAN);

    private final Pattern mPattern;

    public MovieParser() {
        mPattern = Pattern.compile(REGEX_MOVIE);
    }


    public Movie parse(String line) {
        if (!line.startsWith("movies[")) {
            return null;
        }
        Movie movie = new Movie();
        Matcher matcher = mPattern.matcher(line);
        if (matcher.find()) {
            movie.setResourceId(matcher.group(1));
            movie.setTitle(matcher.group(2));
            movie.setReleaseDate(parseDate(FORMAT_RELASE_DATE, matcher.group(3)));
            movie.setDuration(Long.parseLong(matcher.group(4)));
            movie.setContentRating(Integer.parseInt(matcher.group(5)));
            movie.setDescription(matcher.group(6));
        }

        return movie;
    }

    private Calendar parseDate(DateFormat format, String toParse) {
        try {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(format.parse(toParse));
            return startDate;
        } catch (ParseException e) {
            return null;
        }
    }
}
