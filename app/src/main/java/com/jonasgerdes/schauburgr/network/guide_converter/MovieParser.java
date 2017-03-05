package com.jonasgerdes.schauburgr.network.guide_converter;

import android.text.Html;

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

    private static final java.lang.String PATTERN_DATE = "yyMMddHHmm";
    public static final DateFormat FORMAT_DATE
            = new SimpleDateFormat(PATTERN_DATE, Locale.GERMAN);

    //is used on Schauburg Website like this *rolleyes*
    private static final String DATE_NONE_PLACEHOLDER = "9999999999";


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
            movie.setReleaseDate(MovieParser.parseDate(matcher.group(3)));
            movie.setDuration(Long.parseLong(matcher.group(4)));
            movie.setContentRating(Integer.parseInt(matcher.group(5)));
            movie.setDescription(matcher.group(6));

            String rawTitle = matcher.group(2);
            rawTitle = parse3D(rawTitle, movie);
            rawTitle = parseAtmos(rawTitle, movie);
            rawTitle = Html.fromHtml(rawTitle).toString(); //fix &amp; etc
            movie.setTitle(rawTitle);
        }

        return movie;
    }

    private String parse3D(String rawTitle, Movie movie) {
        if (rawTitle.startsWith("(3D) ")) {
            movie.setIs3D(true);
            return rawTitle.substring(5, rawTitle.length());
        }
        return rawTitle;
    }

    private String parseAtmos(String rawTitle, Movie movie) {
        String[] indicators = new String[]{
                " in Dolby Atmos",
                " Dolby Atmos"
        };
        for (String indicator : indicators) {
            if (rawTitle.contains(indicator)) {
                movie.setIsAtmos(true);
                return rawTitle.replace(indicator, "");
            }
        }
        return rawTitle;
    }

    public static Calendar parseDate(String toParse) {
        if (toParse.equals(DATE_NONE_PLACEHOLDER)) {
            return null;
        }
        try {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(FORMAT_DATE.parse(toParse));
            return startDate;
        } catch (ParseException e) {
            return null;
        }
    }
}
