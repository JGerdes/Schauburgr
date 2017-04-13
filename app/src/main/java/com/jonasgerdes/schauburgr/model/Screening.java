package com.jonasgerdes.schauburgr.model;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jonas on 04.03.2017.
 */

public class Screening extends RealmObject {

    @PrimaryKey
    private String resourceId;
    private Movie movie;
    private Date startDate;
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

    public boolean isOnSameDate(Screening other) {
        return getStartDate().toLocalDate()
                .isEqual(other.getStartDate().toLocalDate());
    }


    @Override
    public String toString() {
        return "Screening{" +
                "resourceId='" + resourceId + '\'' +
                ", movie=" + movie +
                ", startDate=" + startDate +
                ", hall=" + hall +
                '}';
    }
}
