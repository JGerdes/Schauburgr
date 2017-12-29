package com.jonasgerdes.schauburgr.model.schauburg;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.jonasgerdes.schauburgr.model.CinemaHost;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * UrlProvider which provides urls to webpages and resources like images on the
 * server of the schauburg website.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @see UrlProvider
 * @since 07.03.2017
 */
public class SchauburgUrlProvider implements UrlProvider, Interceptor {

    private static final String PROTOCOL = "http://";

    private @NonNull
    CinemaHost mHost;

    /**
     * Create a new instance of an UrlProvider for the schauburg website
     *
     * @param host Base url containing a trailing forward slash
     */
    public SchauburgUrlProvider(CinemaHost host) {
        mHost = host;
    }


    public void setHost(CinemaHost host) {
        mHost = host;
    }

    public String getBaseUrl() {
        return PROTOCOL + mHost.getDataUrl() + "/";
    }

    /**
     * Get url of an image file of the poster of given movie. Creates a fingerprint for
     * movie using it's {@link Movie#resourceId} and {@link Movie#title} to enable caching file
     * preventing loading of wrong images.
     *
     * @param movie instance of movie to get the poster url for
     * @return web url to an image file showing a movie poster
     */
    @Override
    public String getPosterImageUrl(Movie movie) {
        //return hd url if existing
        if (movie.getHdPosterUrl() != null) {
            return movie.getHdPosterUrl();
        }
        //encode title as base64 and append it to url to prevent issue when same image is used for
        //different movie while still allowing image to be cached as long as used for same movie
        byte[] titleBytes = movie.getTitle().getBytes();
        String fingerprint = Base64.encodeToString(titleBytes, Base64.URL_SAFE | Base64.NO_PADDING);
        return getBaseUrl() + "generated/" + movie.getResourceId() + ".jpg?f=" + fingerprint;
    }

    /**
     * Creates an url to an webpage which enables visitor to create a reservation and/or by tickets
     * for seats for the given screening.
     *
     * @param screening Screening which should be create a reservation for
     * @return web url to webpage showing reservation/purchase
     */
    @Override
    public String getReservationPageUrl(Screening screening) {
        String url = mHost.getTicketUrl() + "/booking/";
        url += screening.getResourceId();
        return url;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        HttpUrl newUrl = url.newBuilder()
                .host(mHost.getDataUrl())
                .build();

        Request newRequest = request.newBuilder()
                .url(newUrl).build();
        return chain.proceed(newRequest);
    }
}
