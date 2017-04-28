package com.jonasgerdes.schauburgr.dagger.component;

import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgDataLoader;
import com.jonasgerdes.schauburgr.usecase.home.HomeActivity;
import com.jonasgerdes.schauburgr.usecase.home.about.AboutPresenter;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuidePresenter;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuideView;
import com.jonasgerdes.schauburgr.usecase.home.movies.MoviesPresenter;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.CompactMovieHolder;
import com.jonasgerdes.schauburgr.usecase.home.movies.movie_list.FullMovieHolder;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailActivity;
import com.jonasgerdes.schauburgr.usecase.movie_detail.MovieDetailPresenter;
import com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list.ScreeningHolder;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component used to inject dependencies.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(GuidePresenter presenter);

    void inject(MoviesPresenter presenter);

    void inject(FullMovieHolder fullMovieHolder);

    void inject(AboutPresenter aboutPresenter);

    void inject(MovieDetailActivity movieDetailActivity);

    void inject(MovieDetailPresenter movieDetailPresenter);

    void inject(ScreeningHolder screeningHolder);

    void inject(GuideView guideView);

    void inject(CompactMovieHolder compactMovieHolder);

    void inject(SchauburgDataLoader schauburgDataLoader);

    void inject(HomeActivity homeActivity);
}
