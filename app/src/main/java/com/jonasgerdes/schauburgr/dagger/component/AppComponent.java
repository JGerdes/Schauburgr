package com.jonasgerdes.schauburgr.dagger.component;

import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;
import com.jonasgerdes.schauburgr.usecase.home.about.AboutPresenter;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuidePresenter;
import com.jonasgerdes.schauburgr.usecase.home.movies.MoviesPresenter;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieHolder;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailActivity;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailPresenter;
import com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list.ScreeningHolder;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(GuidePresenter presenter);

    void inject(MoviesPresenter presenter);

    void inject(MovieHolder movieHolder);

    void inject(AboutPresenter aboutPresenter);

    void inject(MovieDetailActivity movieDetailActivity);

    void inject(MovieDetailPresenter movieDetailPresenter);

    void inject(ScreeningHolder screeningHolder);
}
