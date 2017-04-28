package com.jonasgerdes.schauburgr.model;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

/**
 * Interface for a UrlProvider which provides urls to webpages or resources likes images.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 07.03.2017
 */

public interface UrlProvider {

    /**
     * Creates an url to an image file showing the poster of given movie.
     * @param movie instance of movie to get the poster url for
     * @return web url to an image file showing a movie poster
     */
    String getPosterImageUrl(Movie movie);

    /**
     * Creates an url to an webpage which enables visitor to create a reservation and/or by tickets
     * for seats for the given screening.
     * @param screening Screening which should be create a reservation for
     * @return web url to webpage showing reservation/purchase
     */
    String getReservationPageUrl(Screening screening);

}
