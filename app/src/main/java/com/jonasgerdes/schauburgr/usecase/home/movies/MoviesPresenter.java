package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgApi;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.MovieCategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesPresenter implements MoviesContract.Presenter {

    @Inject
    SchauburgApi mApi;

    @Inject
    MovieRepository mMovieRepository;

    private MoviesContract.View mView;

    @Override
    public void attachView(MoviesContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        showCachedMovies();
    }

    @Override
    public void detachView() {
    }

    private void showCachedMovies() {

        List<Observable<MovieCategory>> categories = new ArrayList<>();

        categories.add(mMovieRepository.getNewMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_new)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getActionMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_action)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getComedyMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_comedy)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getThrillerMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_thriller)
                        .setSubTitle(R.string.movie_list_category_thriller_subtitle)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getKidsMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_kids)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getMoviesWithExtra(Movie.EXTRA_3D)
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_3d)
                        .setBackground(R.drawable.background_category_3d)
                        .setMovies(movies))
        );


        categories.add(mMovieRepository.getExcessLengthMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_long)
                        .setSubTitle(R.string.movie_list_category_long_subtitle)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getSpecialMovies()
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_specials)
                        .setSubTitle(R.string.movie_list_category_specials_subtitle)
                        .setMovies(movies))
        );

        categories.add(mMovieRepository.getMoviesWithExtra(Movie.EXTRA_ATMOS)
                .map(movies -> new MovieCategory()
                        .setTitle(R.string.movie_list_category_atmos)
                        .setSubTitle(R.string.movie_list_category_atmos_subtitle)
                        .setMovies(movies))
        );

        mView.showMovieCategories(categories);

    }


}
