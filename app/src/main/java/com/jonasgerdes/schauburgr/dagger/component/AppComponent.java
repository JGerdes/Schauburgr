package com.jonasgerdes.schauburgr.dagger.component;

import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;
import com.jonasgerdes.schauburgr.usecase.home.about.AboutPresenter;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuidePresenter;
import com.jonasgerdes.schauburgr.usecase.home.movies.MoviesPresenter;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.MovieHolder;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(GuidePresenter presenter);

    void inject(MoviesPresenter presenter);

    void inject(MovieHolder movieHolder);

    void inject(AboutPresenter aboutPresenter);
}
