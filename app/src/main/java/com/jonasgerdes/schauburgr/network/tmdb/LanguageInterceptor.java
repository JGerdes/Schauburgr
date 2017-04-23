package com.jonasgerdes.schauburgr.network.tmdb;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class LanguageInterceptor implements Interceptor {

    public static final String GERMAN = "de-DE";

    private String mLanguage;

    public LanguageInterceptor(String language) {
        mLanguage = language;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        HttpUrl newUrl = url.newBuilder()
                .addQueryParameter("language", mLanguage)
                .build();

        Request newRequest = request.newBuilder()
                .url(newUrl).build();
        return chain.proceed(newRequest);
    }
}
