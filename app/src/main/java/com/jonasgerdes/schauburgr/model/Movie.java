package com.jonasgerdes.schauburgr.model;

import java.util.Calendar;

/**
 * Created by jonas on 04.03.2017.
 */

public class Movie {

    private String resourceId;
    private String title;
    private Calendar releaseDate;
    private long duration;
    private int contentRating;
    private String description;
    private boolean is3D;


    public String getResourceId() {
        return resourceId;
    }

    public Movie setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public Movie setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public int getContentRating() {
        return contentRating;
    }

    public Movie setContentRating(int contentRating) {
        this.contentRating = contentRating;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Movie setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean is3D() {
        return is3D;
    }

    public Movie setIs3D(boolean is3D) {
        this.is3D = is3D;
        return this;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "resourceId='" + resourceId + '\'' +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", contentRating=" + contentRating +
                ", description='" + description + '\'' +
                ", is3D=" + is3D +
                '}';
    }
}
