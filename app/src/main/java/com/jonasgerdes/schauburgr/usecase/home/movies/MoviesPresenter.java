package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.MovieCategory;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesPresenter implements MoviesContract.Presenter {

    /**
     * Minimun count of movies in a category to actually show the category
     */
    private static final int CATEGORY_SHOW_THRESHOLD = 3;
    @Inject
    SchauburgApi mApi;

    @Inject
    Realm mRealm;

    private MoviesContract.View mView;
    private List<MovieCategory> mCategories = new ArrayList<>();

    @Override
    public void attachView(MoviesContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
        showCachedMovies();
    }

    @Override
    public void detachView() {
        mRealm.close();
    }

    private void showCachedMovies() {

        mCategories.add(new MovieCategory()
                .setTitle(R.string.movie_list_category_new)
                .setMovies(getNewMovies())
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_action)
                .setMovies(getActionMovies())
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_comedy)
                .setMovies(getComedyMovies())
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_thriller)
                .setSubTitle(R.string.movie_list_category_thriller_subtitle)
                .setMovies(getThrillerMovies())
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_kids)
                .setMovies(getKidsMovies())
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_3d)
                .setBackground(R.drawable.background_category_3d)
                .setMovies(getMoviesWithExtra(Movie.EXTRA_3D))
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_atmos)
                .setSubTitle(R.string.movie_list_category_atmos_subtitle)
                .setMovies(getMoviesWithExtra(Movie.EXTRA_ATMOS))
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_long)
                .setSubTitle(R.string.movie_list_category_long_subtitle)
                .setMovies(getExcessLengthMovies())
        );

        mCategories.add(new MovieCategory()
                .setTitle(R.string.movie_list_category_specials)
                .setSubTitle(R.string.movie_list_category_specials_subtitle)
                .setMovies(getSpecialMovies())
        );

        mView.showMovieCategories(mCategories);

    }

    private void addCategoryIfReasonable(MovieCategory category) {
        if (isReasonable(category)) {
            mCategories.add(category);
        }
    }

    private boolean isReasonable(MovieCategory category) {
        return category.getMovies().size() >= CATEGORY_SHOW_THRESHOLD;
    }

    private RealmResults<Movie> getNewMovies() {
        return mRealm.where(Movie.class)
                .greaterThanOrEqualTo("releaseDate", new LocalDate().minusDays(28).toDate())
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getActionMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Action")
                .greaterThanOrEqualTo("contentRating", 12)
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getComedyMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Komödie")
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getThrillerMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Thriller")
                .or()
                .contains("genres", "Horror")
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getKidsMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Animation")
                .or()
                .contains("genres", "Familie")
                .lessThanOrEqualTo("contentRating", 6)
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getMoviesWithExtra(String extra) {
        return mRealm.where(Movie.class)
                .contains("extras", extra)
                .findAllSorted("releaseDate", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getExcessLengthMovies() {
        return mRealm.where(Movie.class)
                .greaterThanOrEqualTo("duration", Movie.DURATION_EXCESS_LENGTH_STATE_1)
                .not().contains("genres", "Met Opera")
                .findAllSorted("duration", Sort.DESCENDING)
                .where().distinct("title");
    }

    private RealmResults<Movie> getSpecialMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Met Opera")
                .or()
                .contains("extras", Movie.EXTRA_REEL)
                .or()
                .contains("extras", Movie.EXTRA_TIP)
                .or()
                .contains("extras", Movie.EXTRA_OT)
                .findAllSorted("releaseDate", Sort.DESCENDING);
    }
}
