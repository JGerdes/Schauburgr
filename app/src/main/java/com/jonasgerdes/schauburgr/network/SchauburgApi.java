package com.jonasgerdes.schauburgr.network;

import com.jonasgerdes.schauburgr.model.Guide;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Defintion of api for schauburg cineworld. Is used by Retrofit to create an actual implementation.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public interface SchauburgApi {

    @GET("generated/data.js")
    Observable<Guide> getFullGuide();
}
