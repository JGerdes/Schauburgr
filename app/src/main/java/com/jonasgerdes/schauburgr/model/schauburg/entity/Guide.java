package com.jonasgerdes.schauburgr.model.schauburg.entity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Model representation of the full screening guide containing both a list of {@link Movie}
 * and a list of {@link Screening}.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public class Guide extends RealmObject{
    /**
     * All movies currently in program of cinema
     */
    private RealmList<Movie> movies = new RealmList<>();

    /**
     * All screenings of current week (typically from thursday to wednesday, updated on mondays)
     */
    private RealmList<Screening> screenings = new RealmList<>();

    public RealmList<Movie> getMovies() {
        return movies;
    }

    public Guide setMovies(RealmList<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public RealmList<Screening> getScreenings() {
        return screenings;
    }

    public Guide setScreenings(RealmList<Screening> screenings) {
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

    /**
     * Groups all screenings by start time and day they are on. See {@link ScreeningDay} for
     * detailed explanation of data structure generated here.
     * @return list of screening days
     */
    public List<ScreeningDay> getScreeningsGroupedByStartTime() {
        List<ScreeningDay> days = new ArrayList<>();
        for (Screening screening : getScreenings()) {
            boolean found = false;
            //search for already created days and add screening if matching day was found
            for (ScreeningDay day : days) {
                if (screening.getStartDate().toLocalDate().isEqual(day.getDate())) {
                    day.addScreening(screening);
                    found = true;
                }
            }
            //if no day matched day of screening, create a new day
            if (!found) {
                days.add(new ScreeningDay()
                        .setDate(screening.getStartDate().toLocalDate())
                        .addScreening(screening)
                );

            }
        }
        return days;
    }


}
