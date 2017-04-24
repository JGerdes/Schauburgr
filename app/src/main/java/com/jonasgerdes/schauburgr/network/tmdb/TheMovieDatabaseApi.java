package com.jonasgerdes.schauburgr.network.tmdb;

import com.jonasgerdes.schauburgr.model.tmdb.SearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Defintion of api for schauburg cineworld. Is used by Retrofit to create an actual implementation.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public interface TheMovieDatabaseApi {

    @GET("search/movie")
    Observable<SearchResponse> search(
            @Query("query") String query
    );

    @GET("search/movie")
    Observable<SearchResponse> search(
            @Query("query") String query,
            @Query("year") int year
    );
}