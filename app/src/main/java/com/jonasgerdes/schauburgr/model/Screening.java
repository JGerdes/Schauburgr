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
}
