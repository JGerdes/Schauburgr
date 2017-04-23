package com.jonasgerdes.schauburgr.model.tmdb;

import java.util.Date;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class SearchResult {
    private String title;
    private int id;
    private String posterPath;
    private String backdropPath;
    private String overview;
    private Date releaseDate;

    public String getTitle() {
        return title;
    }

    public SearchResult setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getId() {
        return id;
    }

    public SearchResult setId(int id) {
        this.id = id;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public SearchResult setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public SearchResult setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public SearchResult setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public SearchResult setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }
}
