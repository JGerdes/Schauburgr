package com.jonasgerdes.schauburgr.model;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model representation of a single movie screening. Contains movie the screening is for as well as
 * the hall it takes place in and the date it's at.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public class Screening extends RealmObject {

    /**
     * Resource id of screening (provided by API), can be used to link to ticket store
     * or seat reservation
     */
    @PrimaryKey
    private String resourceId;

    /**
     * Movie shown during screening
     */
    private Movie movie;

    /**
     * Start date and time of screening
     */
    private Date startDate;

    /**
     * Hall the screening takes place in
     */
    private int hall;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public DateTime getStartDate() {
        return new DateTime(startDate);
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate.toDate();
    }

    public int getHall() {
        return hall;
    }

    public void setHall(int hall) {
        this.hall = hall;
    }

    /**
     * Checks whether a given screening takes place on same date as itself. Ignores screening time.
     * @param other screening to compare with
     * @return true if both screenings take place on same date, false otherwise
     */
    public boolean isOnSameDate(Screening other) {
        return getStartDate().toLocalDate()
                .isEqual(other.getStartDate().toLocalDate());
    }

}
