package com.jonasgerdes.schauburgr.model;

import com.jonasgerdes.schauburgr.util.StringUtil;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model representation of a movie. Contains attributes like title, duration, genres and releaseDate
 * as well as additional information (see {@link #extras}) e.g. if the movie is in 3d,
 * Dolby Atmos, part of "Filmrolle" etc.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public class Movie extends RealmObject {

    public static final String GENRE_MET_OPERA = "Met Opera";

    /**
     * Common extras that are currently parsed and saved
     */
    public static final String EXTRA_3D = "3D";
    public static final String EXTRA_ATMOS = "Atmos";
    public static final String EXTRA_OT = "OT"; //Original-Ton
    public static final String EXTRA_TIP = "Tip";
    public static final String EXTRA_REEL = "Reel"; //"Filmrolle"-Aktion

    /**
     * Separator for concatenated extras and genres. Three semicolons in a row
     * shouldn't be in any genre or extra name.
     */
    private static final String STRING_LIST_SEPARATOR = ";;;";


    /**
     * Resource id of movie (provided by API) to fetch movie poster image
     */
    @PrimaryKey
    private String resourceId;

    /**
     * Title of movie
     */
    private String title;

    /**
     * Release date of movie (usually of release in Germany, may vary though)
     */
    private Date releaseDate;

    /**
     * Duration of movie in seconds
     */
    private long duration;

    /**
     * USK rating of movie (0, 6, 12, 16 or 18)
     */
    private int contentRating;

    /**
     * Description of movie in general (may include actors, duration, content rating)
     * and/or short teaser of the plot of movie. Also may include some additional information
     * regarding the screening (like specials) and/or tickets prices etc.
     */
    private String description;

    /**
     * Concatenated string of all genres of the movie. Might be an empty string.
     * A concatenated string of elements is used instead of RealmList<RealmString> due to reasons
     * described here: https://hackernoon.com/realmlist-realmstring-is-an-anti-pattern-bfc10efb7ca5
     */
    private String genres = "";

    /**
     * Concatenated string of all extras of the movie. Might by empty.
     * See {@link #genres} for explanation why a concatenated string is used.
     */
    private String extras = "";

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
        return new DateTime(releaseDate);
    }

    public Movie setReleaseDate(DateTime releaseDate) {
        if (releaseDate == null) {
            this.releaseDate = null;
        } else {
            this.releaseDate = releaseDate.toDate();
        }
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
        return Collections.unmodifiableList(Arrays.asList(extras.split(STRING_LIST_SEPARATOR)));
    }

    public void setExtras(List<String> extras) {
        this.extras = StringUtil.concat(extras, STRING_LIST_SEPARATOR);
    }

    public List<String> getGenres() {
        String genres = this.genres;
        return Collections.unmodifiableList(Arrays.asList(genres.split(STRING_LIST_SEPARATOR)));
    }

    public void setGenres(List<String> genres) {
        this.genres = StringUtil.concat(genres, STRING_LIST_SEPARATOR);
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
