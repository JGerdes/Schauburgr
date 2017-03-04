package com.jonasgerdes.schauburgr.model;

/**
 * Created by jonas on 04.03.2017.
 */

public class Movie {

    private String resourceId;
    private String title;
    private String releaseDate;
    private long duration;
    private int contentRating;
    private String description;
    private boolean is3D;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getContentRating() {
        return contentRating;
    }

    public void setContentRating(int contentRating) {
        this.contentRating = contentRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean is3D() {
        return is3D;
    }

    public void setIs3D(boolean is3D) {
        this.is3D = is3D;
    }
}
