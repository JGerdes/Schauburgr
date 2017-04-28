package com.jonasgerdes.schauburgr.model.schauburg.entity;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.04.2017
 */

public class MovieCategory {

    private @StringRes int title;
    private @StringRes int subTitle = -1;
    private @DrawableRes int background = -1;
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
