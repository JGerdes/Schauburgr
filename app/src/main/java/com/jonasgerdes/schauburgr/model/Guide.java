package com.jonasgerdes.schauburgr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 04.03.2017.
 */

public class Guide {
    private List<Movie> movies = new ArrayList<>();
    private List<Screening> screenings = new ArrayList<>();

    public List<Movie> getMovies() {
        return movies;
    }

    public Guide setMovies(List<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public Guide setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
        return this;
    }

    public Guide addMovie(Movie movie) {
        movies.add(movie);
        return this;
    }

    public Guide addScreening(Screening screening) {
        screenings.add(screening);
        return this;
    }

    public List<ScreeningDay> getScreeningsGroupedByStartTime() {
        List<ScreeningDay> days = new ArrayList<>();
        for (Screening screening : getScreenings()) {
            boolean found = false;
            for (ScreeningDay day : days) {
                if (screening.isOnDay(day.getDate())) {
                    day.addScreening(screening);
                    found = true;
                }
            }
            if (!found) {
                days.add(new ScreeningDay()
                        .setDate(screening.getStartDate())
                        .addScreening(screening)
                );

            }
        }
        return days;
    }


}
