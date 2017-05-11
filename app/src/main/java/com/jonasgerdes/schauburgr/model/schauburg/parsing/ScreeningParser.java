package com.jonasgerdes.schauburgr.model.schauburg.parsing;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for screenings retrieved via schauburg-cineworld.de "API".
 * Create instances of {@link Screening} based on single lines which define a screening as
 * JavaScript object (via a constructor though, not as JSON)
 * Sample screening definition:
 * timetable[0] = new seance('12','1703051400','43161','0','1');
 * Attributes like movie-id, start date/time, ressource-id, etc are extracted via regex.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
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

    /**
     * Creates a {@link Screening} instance by parsing give line from "API"
     * @param line single JavaScript line calling screening constructor (as used in schauburg "API")
     * @return new instance of {@link Screening} with data parse from line or null if parsing fails
     */
    public Screening parse(String line, List<Movie> movies) {
        //in the javascript to parse, all screenings are put in an array called "timetable", so this
        //is the simples way to test whether we have a line containing a screening construction and
        //can parse it
        if (!line.startsWith("timetable[")) {
            return null;
        }
        Screening screening = new Screening();
        Matcher matcher = mPattern.matcher(line);
        if (matcher.find()) {
            screening.setResourceId(matcher.group(3));
            screening.setStartDate(SchauburgGuideConverter.parseDate(matcher.group(2)));
            screening.setHall(Integer.parseInt(matcher.group(4)));

            int movieId = Integer.parseInt(matcher.group(1));
            screening.setMovie(movies.get(movieId));
        }

        return screening;
    }

}
