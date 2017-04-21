package com.jonasgerdes.schauburgr.dagger.module;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonasgerdes.schauburgr.network.SchauburgApi;
import com.jonasgerdes.schauburgr.network.guide_converter.SchauburgGuideConverter;
import com.jonasgerdes.schauburgr.network.url.UrlProvider;
import com.jonasgerdes.schauburgr.network.url.SchauburgUrlProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module providing data related dependencies like database and network accessors
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

@Module
public class DataModule {
    String mBaseUrl;

    public DataModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(new SchauburgGuideConverter.Factory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    SchauburgApi provideSchauburgApi(Retrofit retrofit) {
        return retrofit.create(SchauburgApi.class);
    }

    @Provides
    @Singleton
    UrlProvider provideImageUrlCreator() {
        return new SchauburgUrlProvider(mBaseUrl);
    }

    /**
     * Provides a not singleton instance of realm db. Instance can and should be closed via
     * {@link Realm#close()} when not used any more
     * @return A instance of default realm database
     */
    @Provides
    Realm provideRealm() {
       return Realm.getDefaultInstance();
    }


}
