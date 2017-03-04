package com.jonasgerdes.schauburgr.network.guide_converter;

import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jonas on 04.03.2017.
 */

public class ScreeningParser {

    private static final String REGEX_PREFIX = "new seance\\(";
    private static final String REGEX_MOVIE_ID = "'(\\d{1,3})'";
    private static final String REGEX_TIME = "'(\\d{10})'";
    private static final String REGEX_RES_ID = "'(\\d+?)'";
    private static final String REGEX_HALL = "'(\\d)'";
    private static final String REGEX_STUB = "'.*?'";

    private static final String REGEX_SCREENING = ""
            + REGEX_PREFIX
            + REGEX_MOVIE_ID + ','
            + REGEX_TIME + ','
            + REGEX_RES_ID + ','
            + REGEX_STUB + ','
            + REGEX_HALL + "\\);";

    private final Pattern mPattern;

    public ScreeningParser() {
        mPattern = Pattern.compile(REGEX_SCREENING);
    }


    public Screening parse(String line, List<Movie> movies) {
        if (!line.startsWith("timetable[")) {
            return null;
        }
        Screening screening = new Screening();
        Matcher matcher = mPattern.matcher(line);
        if (matcher.find()) {
            screening.setResourceId(matcher.group(3));
            screening.setStartDate(MovieParser.parseDate(matcher.group(2)));
            screening.setHall(Integer.parseInt(matcher.group(4)));

            int movieId = Integer.parseInt(matcher.group(1));
            screening.setMovie(movies.get(movieId));
        }

        return screening;
    }

}
