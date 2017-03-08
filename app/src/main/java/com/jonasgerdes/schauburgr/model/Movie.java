package com.jonasgerdes.schauburgr.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 04.03.2017.
 */

public class Movie {

    public static final String GENRE_MET_OPERA = "Met Opera";


    private String resourceId;
    private String title;
    private DateTime releaseDate;
    private long duration;
    private int contentRating;
    private String description;
    private List<String> genres = new ArrayList<>();
    private boolean is3D;
    private boolean isAtmos;
    private boolean isOT; //Original-Ton
    private boolean isTip;
    private boolean isReel; //"Filmrolle"-Aktion


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

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(DateTime releaseDate) {
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public boolean is3D() {
        return is3D;
    }

    public void set3D(boolean is3D) {
        this.is3D = is3D;
    }

    public boolean isAtmos() {
        return isAtmos;
    }

    public void setAtmos(boolean atmos) {
        isAtmos = atmos;
    }

    public boolean isOT() {
        return isOT;
    }

    public void setOT(boolean OT) {
        isOT = OT;
    }

    public boolean isTip() {
        return isTip;
    }

    public void setTip(boolean tip) {
        isTip = tip;
    }

    public boolean isReel() {
        return isReel;
    }

    public void setReel(boolean reel) {
        isReel = reel;
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
                ", isAtmos=" + isAtmos +
                ", isOT=" + isOT +
                ", isTip=" + isTip +
                ", isReel=" + isReel +
                '}';
    }
}
