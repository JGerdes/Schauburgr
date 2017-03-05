package com.jonasgerdes.schauburgr.network.guide_converter;

import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;

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
 * Created by jonas on 04.03.2017.
 * Website of Schauburg Cineworld is kinda weird, so we have to parse JavaScript here :(
 */

public class SchauburgGuideConverter implements Converter<ResponseBody, Guide> {
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static final class Factory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit) {
            return INSTANCE;
        }

    }

    private static final SchauburgGuideConverter INSTANCE = new SchauburgGuideConverter();

    private final MovieParser mMovieParser;
    private final ScreeningParser mScreeningParser;

    private SchauburgGuideConverter() {
        mMovieParser = new MovieParser();
        mScreeningParser = new ScreeningParser();
    }

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

}
