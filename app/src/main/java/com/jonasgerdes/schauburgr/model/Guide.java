package com.jonasgerdes.schauburgr.model;

import java.util.List;

/**
 * Created by jonas on 04.03.2017.
 */

public class Guide {
    private List<Movie> movies;
    private List<Screening> screenings;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }
}
