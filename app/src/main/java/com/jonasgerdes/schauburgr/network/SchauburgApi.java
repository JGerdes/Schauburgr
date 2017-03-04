package com.jonasgerdes.schauburgr.network;

import com.jonasgerdes.schauburgr.model.Guide;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jonas on 04.03.2017.
 */

public interface SchauburgApi {

    @GET("generated/data.js")
    Call<Guide> getFullGuide();
}
