package com.jonasgerdes.schauburgr.model.tmdb;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.tmdb.entity.search.SearchResponse;
import com.jonasgerdes.schauburgr.model.tmdb.entity.search.SearchResult;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 28.04.2017
 */

public class TheMovieDatabaseDataLoader {

    TheMovieDatabaseApi mTheMovieDatabaseApi;

    public TheMovieDatabaseDataLoader(TheMovieDatabaseApi theMovieDatabaseApi) {
        mTheMovieDatabaseApi = theMovieDatabaseApi;
    }

    public Observable<Movie> searchAndSaveMovie(Movie movie) {
        return mTheMovieDatabaseApi.search(movie.getTitle(), 2017)
                .map(SearchResponse::getResults)
                .doOnNext(results -> setTMDbId(movie, results))
                .map(results -> movie);
    }

    private void setTMDbId(Movie movie, List<SearchResult> results) {
        if (results.size() == 0) {
            return;
        }
        SearchResult result = results.get(0);
        movie.setTmdbId(result.getId());
        movie.setReleaseDate(result.getReleaseDate());
        movie.setHdPosterUrl("https://image.tmdb.org/t/p/w500" + result.getPosterPath());
        movie.setCoverUrl("https://image.tmdb.org/t/p/w780" + result.getBackdropPath());
        try (Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction((realm) -> {
                realm.copyToRealmOrUpdate(movie);
            });
        }
    }
}
