package com.jonasgerdes.schauburgr.model.tmdb;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor adding an query parameter for language
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class LanguageInterceptor implements Interceptor {

    private static final String PARAMETER_LANGUAGE = "language";
    public static final String GERMAN = "de-DE";

    private String mLanguage;

    /**
     * @param language Language tag to send (ISO 639-1)
     */
    public LanguageInterceptor(String language) {
        mLanguage = language;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        HttpUrl newUrl = url.newBuilder()
                .addQueryParameter(PARAMETER_LANGUAGE, mLanguage)
                .build();

        Request newRequest = request.newBuilder()
                .url(newUrl).build();
        return chain.proceed(newRequest);
    }
}
