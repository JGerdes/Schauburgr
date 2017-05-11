package com.jonasgerdes.schauburgr.model.tmdb;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor adding an query parameter for an api key
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class ApiKeyInterceptor implements Interceptor {
    private static final String PARAMETER_API_KEY = "api_key";
    private String mApiKey;

    /**
     *
     * @param apiKey Api key for TMDb
     */
    public ApiKeyInterceptor(String apiKey) {
        mApiKey = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        HttpUrl newUrl = url.newBuilder()
                .addQueryParameter(PARAMETER_API_KEY, mApiKey)
                .build();

        Request newRequest = request.newBuilder()
                .url(newUrl).build();
        return chain.proceed(newRequest);
    }
}
