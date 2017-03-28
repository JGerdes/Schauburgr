package com.jonasgerdes.schauburgr.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by jonas on 04.03.2017.
 */

public class Guide extends RealmObject{
    private RealmList<Movie> movies = new RealmList<>();
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

    public List<ScreeningDay> getScreeningsGroupedByStartTime() {
        List<ScreeningDay> days = new ArrayList<>();
        for (Screening screening : getScreenings()) {
            boolean found = false;
            for (ScreeningDay day : days) {
                if (screening.getStartDate().toLocalDate().isEqual(day.getDate())) {
                    day.addScreening(screening);
                    found = true;
                }
            }
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
