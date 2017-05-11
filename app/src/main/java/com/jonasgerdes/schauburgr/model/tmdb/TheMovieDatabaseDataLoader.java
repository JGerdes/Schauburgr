package com.jonasgerdes.schauburgr.model.tmdb;

import android.util.Log;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.tmdb.entity.search.SearchResponse;
import com.jonasgerdes.schauburgr.model.tmdb.entity.search.SearchResult;
import com.jonasgerdes.schauburgr.model.tmdb.entity.video.Video;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * DataLoader utilizing {@link TheMovieDatabaseApi} to fetch movie data from TMDb server
 *
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

    public Observable<Movie> getVideosAndSave(Movie movie) {
        String resourceId = movie.getResourceId();
        return mTheMovieDatabaseApi.getVideos(movie.getTmdbId())
                .subscribeOn(Schedulers.io())
                .map(videoResponse -> {
                    Log.d(TAG, "getVideosAndSave: " + videoResponse.getResults().size());
                    RealmList<Video> videos = new RealmList<>();
                    videos.addAll(videoResponse.getResults());
                    return videos;
                })
                //add to movie
                .map(videos -> {
                    try (Realm r = Realm.getDefaultInstance()) {
                        r.executeTransaction((realm) -> {
                            RealmList<Video> managedVideos = new RealmList<>();
                            managedVideos.addAll(realm.copyToRealm(videos));
                            realm.where(Movie.class)
                                    .equalTo("resourceId", resourceId)
                                    .findFirst()
                                    .setVideos(managedVideos);
                        });
                    }
                    return movie;
                });
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
