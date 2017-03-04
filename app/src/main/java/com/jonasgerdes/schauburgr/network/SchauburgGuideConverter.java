package com.jonasgerdes.schauburgr.network;

import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by jonas on 04.03.2017.
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

    private SchauburgGuideConverter() {

    }

    @Override
    public Guide convert(ResponseBody value) throws IOException {
        Guide guide = new Guide();
        guide.setMovies(new ArrayList<Movie>());
        guide.setScreenings(new ArrayList<Screening>());
        return guide;
    }
}
