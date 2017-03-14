package com.jonasgerdes.schauburgr.network.image;

import android.util.Base64;

import com.jonasgerdes.schauburgr.model.Movie;

/**
 * Created by jonas on 07.03.2017.
 */

public class SchauburgImageUrlCreator implements ImageUrlCreator {

    private String mBaseUrl;

    public SchauburgImageUrlCreator(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Override
    public String getPosterImageUrl(Movie movie) {
        //Encode title as Base64 and append it to url to prevent issue when same image is used for
        //different movie while still allowing image to be cached as long as used for same movie
        byte[] titleBytes = movie.getTitle().getBytes();
        String fingerprint = Base64.encodeToString(titleBytes, Base64.URL_SAFE | Base64.NO_PADDING);
        return mBaseUrl + "generated/" + movie.getResourceId() + ".jpg?f=" + fingerprint;
    }
}
