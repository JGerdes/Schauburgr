package com.jonasgerdes.schauburgr.dagger.module;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonasgerdes.schauburgr.BuildConfig;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgApi;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgDataLoader;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgUrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.guide_converter.SchauburgGuideConverter;
import com.jonasgerdes.schauburgr.model.tmdb.ApiKeyInterceptor;
import com.jonasgerdes.schauburgr.model.tmdb.DateDeserializer;
import com.jonasgerdes.schauburgr.model.tmdb.LanguageInterceptor;
import com.jonasgerdes.schauburgr.model.tmdb.TheMovieDatabaseApi;
import com.jonasgerdes.schauburgr.model.tmdb.TheMovieDatabaseDataLoader;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module providing data related dependencies like database and network accessors
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

@Module
public class DataModule {
    String mSchauburgBaseUrl;
    String mTheMovieDatabaseBaseUrl;

    public DataModule(String schauburgBaseUrl, String theMovieDatabaseBaseUrl) {
        mSchauburgBaseUrl = schauburgBaseUrl;
        mTheMovieDatabaseBaseUrl = theMovieDatabaseBaseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateDeserializer());
        return gsonBuilder.create();
    }


    @Provides
    @Singleton
    SchauburgApi provideSchauburgApi(Gson gson) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(new SchauburgGuideConverter.Factory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mSchauburgBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit.create(SchauburgApi.class);
    }

    @Provides
    @Singleton
    TheMovieDatabaseApi provideTheMovieDatabaseApi(Gson gson) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ApiKeyInterceptor(BuildConfig.API_KEY_TMDB))
                .addInterceptor(new LanguageInterceptor(LanguageInterceptor.GERMAN))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mTheMovieDatabaseBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit.create(TheMovieDatabaseApi.class);
    }

    @Provides
    @Singleton
    UrlProvider provideImageUrlCreator() {
        return new SchauburgUrlProvider(mSchauburgBaseUrl);
    }


    @Provides
    @Singleton
    MovieRepository provideMovieRepository(SchauburgApi schauburgApi, TheMovieDatabaseApi tMDbApi) {
        return new MovieRepository(
                new SchauburgDataLoader(schauburgApi),
                new TheMovieDatabaseDataLoader(tMDbApi)
        );
    }


}
