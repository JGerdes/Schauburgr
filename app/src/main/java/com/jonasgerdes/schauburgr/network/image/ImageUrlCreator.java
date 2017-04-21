package com.jonasgerdes.schauburgr.network.image;

import com.jonasgerdes.schauburgr.model.Movie;

/**
 * Interface for a ImageUrlCreator which provides a url to an image for a movie poster by passing
 * a movie instance.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 07.03.2017
 */

public interface ImageUrlCreator {

    /**
     * Creates an url to an image file showing the poster of given movie.
     * @param movie instance of movie to get the poster url for
     * @return web url to an image file showing a movie poster
     */
    String getPosterImageUrl(Movie movie);

}
