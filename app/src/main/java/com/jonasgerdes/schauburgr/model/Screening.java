package com.jonasgerdes.schauburgr.model;

import java.util.Calendar;

/**
 * Created by jonas on 04.03.2017.
 */

public class Screening {

    private String resourceId;
    private Movie movie;
    private Calendar startDate;
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

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public int getHall() {
        return hall;
    }

    public void setHall(int hall) {
        this.hall = hall;
    }

    public boolean isOnDay(Calendar day) {
        return getStartDate().get(Calendar.YEAR) == day.get(Calendar.YEAR)
                && getStartDate().get(Calendar.MONTH) == day.get(Calendar.MONTH)
                && getStartDate().get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH);
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
