package com.jonasgerdes.schauburgr.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 04.03.2017.
 */

public class Movie {

    public static final String GENRE_MET_OPERA = "Met Opera";

    public static final String EXTRA_3D = "3D";
    public static final String EXTRA_ATMOS = "Atmos";
    public static final String EXTRA_OT = "OT"; //Original-Ton
    public static final String EXTRA_TIP = "Tip";
    public static final String EXTRA_REEL = "Reel"; //"Filmrolle"-Aktion


    private String resourceId;
    private String title;
    private DateTime releaseDate;
    private long duration;
    private int contentRating;
    private String description;
    private List<String> genres = new ArrayList<>();
    private List<String> extras = new ArrayList<>();


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

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public boolean is3D() {
        return extras.contains(EXTRA_3D);
    }

    public boolean isAtmos() {
        return extras.contains(EXTRA_ATMOS);
    }

    public boolean isOT() {
        return extras.contains(EXTRA_OT);
    }

    public boolean isTip() {
        return extras.contains(EXTRA_TIP);
    }

    public boolean isReel() {
        return extras.contains(EXTRA_REEL);
    }

}
