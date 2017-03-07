package com.jonasgerdes.schauburgr.network.image;

import com.jonasgerdes.schauburgr.model.Movie;

/**
 * Created by jonas on 07.03.2017.
 */

public interface ImageUrlCreator {

    String getPosterImageUrl(Movie movie);

}
