package com.jonasgerdes.schauburgr.model.schauburg.entity;

import com.jonasgerdes.schauburgr.model.tmdb.entity.video.Video;
import com.jonasgerdes.schauburgr.util.StringUtil;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
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

    /**
     * Used when movie hasn't got any TMDb id (or no matching movie was found for title)
     */
    public static final int NO_ID = -1;

    /**
     * Duration at which movie starts to be of excess length
     */
    public static final long DURATION_EXCESS_LENGTH_STATE_1 = 125;

    /**
     * Duration at which movie starts to be in advanced excess length
     */
    public static final long DURATION_EXCESS_LENGTH_STATE_2 = 140;

    public static final String GENRE_MET_OPERA = "Met Opera";
    /**
     * Common extras that are currently parsed and saved
     */
    public static final String EXTRA_3D = "3D";
    public static final String EXTRA_ATMOS = "ATMOS";
    public static final String EXTRA_OT = "OT"; //Original-Ton
    public static final String EXTRA_TIP = "TIP";

    public static final String EXTRA_REEL = "REEL"; //"Filmrolle"-Aktion
    public static final String EXTRA_LADIES_NIGHT = "LADIES_NIGHT"; //Ladies-Night-Aktion
    public static final String EXTRA_LAST_SCREENINGS = "LAST_SCREENINGS";
    public static final String EXTRA_PREVIEW = "PREVIEW"; //Vorpremiere
    public static final String EXTRA_IGNORE = "IGNORE"; //extra to not be saved
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
     * Id in TheMovieDatabase
     */
    private int tmdbId = NO_ID;

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
     * Concatenated string of directors of the movie. Might by empty.
     * See {@link #genres} for explanation why a concatenated string is used.
     */
    private String directors = "";

    /**
     * Concatenated string of cast of the movie. Might by empty.
     * See {@link #genres} for explanation why a concatenated string is used.
     */
    private String cast = "";

    /**
     * Concatenated string of all extras of the movie. Might by empty.
     * See {@link #genres} for explanation why a concatenated string is used.
     */
    private String extras = "";

    /**
     * Url to a high definition version of movie poster
     */
    private String hdPosterUrl;

    /**
     * Url to an image which shows some scene or landscape poster to be used in background
     */
    private String coverUrl;

    /**
     * List of related videos (trailer, teaser, etc)
     */
    private RealmList<Video> videos = new RealmList<>();

    /**
     * Screenings of this movie
     */
    @LinkingObjects("movie")
    private final RealmResults<Screening> screenings = null;

    public String getResourceId() {
        return resourceId;
    }

    public Movie setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public Movie setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
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

    public Movie setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
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

    public List<String> getGenres() {
        return Collections.unmodifiableList(
                StringUtil.splitWithoutEmpty(genres, STRING_LIST_SEPARATOR)
        );
    }

    public void setGenres(List<String> genres) {
        this.genres = StringUtil.concat(genres, STRING_LIST_SEPARATOR);
    }

    public List<String> getDirectors() {
        return Collections.unmodifiableList(
                StringUtil.splitWithoutEmpty(directors, STRING_LIST_SEPARATOR)
        );
    }

    public void setDirectors(List<String> directors) {
        this.directors = StringUtil.concat(directors, STRING_LIST_SEPARATOR);
    }

    public List<String> getCast() {
        return Collections.unmodifiableList(
                StringUtil.splitWithoutEmpty(cast, STRING_LIST_SEPARATOR)
        );
    }

    public void setCast(List<String> cast) {
        this.cast = StringUtil.concat(cast, STRING_LIST_SEPARATOR);
    }

    public List<String> getExtras() {
        return Collections.unmodifiableList(
                StringUtil.splitWithoutEmpty(extras, STRING_LIST_SEPARATOR)
        );
    }

    public void setExtras(List<String> extras) {
        this.extras = StringUtil.concat(extras, STRING_LIST_SEPARATOR);
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

    public RealmResults<Screening> getScreenings() {
        return screenings;
    }

    public String getHdPosterUrl() {
        return hdPosterUrl;
    }

    public Movie setHdPosterUrl(String hdPosterUrl) {
        this.hdPosterUrl = hdPosterUrl;
        return this;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public Movie setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        return this;
    }

    public RealmList<Video> getVideos() {
        return videos;
    }

    public Movie setVideos(RealmList<Video> videos) {
        this.videos = videos;
        return this;
    }
}
