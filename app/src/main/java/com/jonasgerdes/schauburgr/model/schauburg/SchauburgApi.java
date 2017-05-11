package com.jonasgerdes.schauburgr.model.schauburg;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Guide;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Definition of api for schauburg cineworld. Is used by Retrofit to create an actual implementation.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public interface SchauburgApi {

    @GET("generated/data.js")
    Observable<Guide> getFullGuide();
}
