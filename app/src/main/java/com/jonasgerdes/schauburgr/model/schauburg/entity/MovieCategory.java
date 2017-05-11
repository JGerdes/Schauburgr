package com.jonasgerdes.schauburgr.model.schauburg.entity;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import io.realm.RealmResults;

/**
 * A category of movies of similar type. Can be a specific genre, or a bunch of movies grouped
 * together due to attributes like 3D screenings etc.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class MovieCategory {

    /**
     * String resource of a title for the category,
     * describing the attribute all movies in this category have.
     */
    private @StringRes int title;

    /**
     * Optional string resource of a subtitle for the category describing contained movies
     * more detailed
     */
    private @StringRes int subTitle = -1;

    /**
     * Optional drawable resource to be shown in background of the list of movies
     */
    private @DrawableRes int background = -1;

    /**
     * Movies for this category
     */
    private RealmResults<Movie> movies;

    public int getTitle() {
        return title;
    }

    public MovieCategory setTitle(int title) {
        this.title = title;
        return this;
    }

    public int getSubTitle() {
        return subTitle;
    }

    public MovieCategory setSubTitle(int subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public int getBackground() {
        return background;
    }

    public MovieCategory setBackground(int background) {
        this.background = background;
        return this;
    }

    public RealmResults<Movie> getMovies() {
        return movies;
    }


    public MovieCategory setMovies(RealmResults<Movie> movies) {
        this.movies = movies;
        return this;
    }
}
