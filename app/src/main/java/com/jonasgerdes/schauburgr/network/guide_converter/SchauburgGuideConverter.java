package com.jonasgerdes.schauburgr.network.guide_converter;

import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by jonas on 04.03.2017.
 * Website of Schauburg Cineworld is kinda weird, so we have to parse JavaScript here :(
 */

public class SchauburgGuideConverter implements Converter<ResponseBody, Guide> {

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

    private SchauburgGuideConverter() {
        mMovieParser = new MovieParser();
    }

    @Override
    public Guide convert(ResponseBody value) throws IOException {
        Guide guide = new Guide();
        guide.setMovies(new ArrayList<Movie>());
        guide.setScreenings(new ArrayList<Screening>());

        BufferedReader reader = new BufferedReader(value.charStream());

        String line = reader.readLine();
        Movie movie;
        Screening screening;
        while (line != null) {
            //try screen parsing first increases performance
            //since there are more screenings then movies
            screening = parseScreening(line);
            if (screening != null) {
                guide.getScreenings().add(screening);
            } else {
                movie = mMovieParser.parse(line);
                if (movie != null) {
                    guide.getMovies().add(movie);
                }
            }
            line = reader.readLine();
        }

        return guide;
    }


    private Screening parseScreening(String line) {
        return null;
    }
}
