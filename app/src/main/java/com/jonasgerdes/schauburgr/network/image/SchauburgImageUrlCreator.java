package com.jonasgerdes.schauburgr.network.image;

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
        return mBaseUrl + "generated/" + movie.getResourceId() + ".jpg";
    }
}
