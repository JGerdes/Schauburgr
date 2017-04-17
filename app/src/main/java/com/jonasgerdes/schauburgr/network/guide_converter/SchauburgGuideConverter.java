package com.jonasgerdes.schauburgr.network.guide_converter;

import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Retrofit converter to parse data from website of Schauburg Cineworld.
 * Unfortunately there is neither a REST-API or some other way to get data as JSON or XML and
 * also the data isn't rendered as HTML server side. It appears like there is a JavaScript
 * file which contains all movie and screening definitions which is dynamically created instead.
 * This file doesn't define data as JSON though, but there is JavaScript code which fills two arrays
 * by calling constructors of "movie" or "seance" (or screening for better wording).
 * Instead of doing something fancy (and possible overkill) like running an actual JavaScript
 * interpreter, we simply walk through the JavaScript code line by line and try to parse movie and
 * screening data with regular expressions.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public class SchauburgGuideConverter implements Converter<ResponseBody, Guide> {
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormat.forPattern("yyMMddHHmm");

    //is used on Schauburg website like this *rolleyes*
    private static final String DATE_NONE_PLACEHOLDER = "9999999999";

    /**
     * Factory to get an instance of a SchauburgGuideConverter
     */
    public static final class Factory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit) {
            return INSTANCE;
        }

    }

    /**
     * Static instance returned by factory
     */
    private static final SchauburgGuideConverter INSTANCE = new SchauburgGuideConverter();

    /**
     * Parser to create {@link Movie} instances
     */
    private final MovieParser mMovieParser;

    /**
     * Parser to create {@link Screening} instances
     */
    private final ScreeningParser mScreeningParser;

    private SchauburgGuideConverter() {
        mMovieParser = new MovieParser();
        mScreeningParser = new ScreeningParser();
    }

    /**
     * Converts the given {@link ResponseBody} containing movie and screening data to an instance
     * of {@link Guide} containing all relevant data by going through each line and try parsing them
     * as either {@link Movie} or {@link Screening}.
     * @param value reponse body (should be plain text/JavaScript) provided by Schauburgs website
     * @return Guide data parsed from given response text
     * @throws IOException Thrown when response body can't be read as text
     */
    @Override
    public Guide convert(ResponseBody value) throws IOException {
        Guide guide = new Guide();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(value.byteStream(), ISO_8859_1)
        );

        String line = reader.readLine();
        Movie movie;
        Screening screening;
        while (line != null) {
            //try screen parsing first increases performance
            //since there are more screenings then movies
            screening = mScreeningParser.parse(line, guide.getMovies());
            if (screening != null) {
                guide.addScreening(screening);
            } else {
                movie = mMovieParser.parse(line);
                if (movie != null) {
                    guide.addMovie(movie);
                }
            }
            line = reader.readLine();
        }

        return guide;
    }


    /**
     * Parses a string containing a date in the format defined in {@link #DATE_NONE_PLACEHOLDER}
     * (which is used in JavaScript source code on Schauburgs website)
     * @param toParse date string to parse
     * @return new instance of {@link DateTime}
     *          or null if invalid format or place holder for no date
     */
    public static DateTime parseDate(String toParse) {
        if (toParse.equals(DATE_NONE_PLACEHOLDER)) {
            return null;
        }
        return new DateTime(FORMAT_DATE.parseDateTime(toParse));
    }

}
