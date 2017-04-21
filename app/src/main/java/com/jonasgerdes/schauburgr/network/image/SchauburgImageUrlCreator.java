package com.jonasgerdes.schauburgr.network.image;

import android.util.Base64;

import com.jonasgerdes.schauburgr.model.Movie;

/**
 * Implementation of a ImageUrlCreator which provides a url to an image for a movie poster on the
 * server of the schauburg website by passing a movie instance.
 *
 * @see ImageUrlCreator
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 07.03.2017
 */
public class SchauburgImageUrlCreator implements ImageUrlCreator {

    /**
     * Base url containing a trailing forward slash (e.g. "http://schauburg-cineworld.com/")
     */
    private String mBaseUrl;

    /**
     * Create a new instance of an ImageUrlCreator for the schauburg webiste
     * @param baseUrl Base url containing a trailing forward slash
     */
    public SchauburgImageUrlCreator(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    /**
     * Get url of an image file of the poster of given movie. Creates a fingerprint for
     * movie using it's {@link Movie#resourceId} and {@link Movie#title} to enable caching file
     * preventing loading of wrong images.
     * @param movie instance of movie to get the poster url for
     * @return web url to an image file showing a movie poster
     */
    @Override
    public String getPosterImageUrl(Movie movie) {
        //encode title as base64 and append it to url to prevent issue when same image is used for
        //different movie while still allowing image to be cached as long as used for same movie
        byte[] titleBytes = movie.getTitle().getBytes();
        String fingerprint = Base64.encodeToString(titleBytes, Base64.URL_SAFE | Base64.NO_PADDING);
        return mBaseUrl + "generated/" + movie.getResourceId() + ".jpg?f=" + fingerprint;
    }
}
