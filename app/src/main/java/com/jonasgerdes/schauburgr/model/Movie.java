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
 * Created by jonas on 04.03.2017.
 */

public class Movie extends RealmObject {

    public static final String GENRE_MET_OPERA = "Met Opera";

    public static final String EXTRA_3D = "3D";
    public static final String EXTRA_ATMOS = "Atmos";
    public static final String EXTRA_OT = "OT"; //Original-Ton
    public static final String EXTRA_TIP = "Tip";
    public static final String EXTRA_REEL = "Reel"; //"Filmrolle"-Aktion

    private static final String STRING_LIST_SEPERATOR = ";;;";


    @PrimaryKey
    private String resourceId;
    private String title;
    private Date releaseDate;
    private long duration;
    private int contentRating;
    private String description;

    //Use concatenated string of elements instead of RealmList<RealmString>
    //see https://hackernoon.com/realmlist-realmstring-is-an-anti-pattern-bfc10efb7ca5
    private String genres = "";
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
        return Collections.unmodifiableList(Arrays.asList(extras.split(STRING_LIST_SEPERATOR)));
    }

    public void setExtras(List<String> extras) {
        this.extras = StringUtil.concat(extras, STRING_LIST_SEPERATOR);
    }

    public List<String> getGenres() {
        String genres = this.genres;
        return Collections.unmodifiableList(Arrays.asList(genres.split(STRING_LIST_SEPERATOR)));
    }

    public void setGenres(List<String> genres) {
        this.genres = StringUtil.concat(genres, STRING_LIST_SEPERATOR);
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
