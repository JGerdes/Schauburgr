package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.MovieCategory;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private Call<Guide> mPendingCall;
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
        if (mPendingCall != null) {
            mPendingCall.cancel();
        }
        mRealm.close();
    }

    private void showCachedMovies() {
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
                .setMovies(getMoviesWithExtra(Movie.EXTRA_3D))
        );

        addCategoryIfReasonable(new MovieCategory()
                .setTitle(R.string.movie_list_category_atmos)
                .setSubTitle(R.string.movie_list_category_atmos_subtitle)
                .setMovies(getMoviesWithExtra(Movie.EXTRA_ATMOS))
        );

        addCategoryIfReasonable(new MovieCategory()
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

    @Override
    public void onRefreshTriggered() {
        mPendingCall = mApi.getFullGuide();
        mPendingCall.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                final List<Movie> movies = response.body().getMovies();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(movies);
                    }
                });
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                if (t instanceof SocketTimeoutException
                        || t instanceof UnknownHostException) {
                    mView.showError("Keine Internetverbindung :(");
                } else {
                    mView.showError(t.getClass().getName());
                }
            }
        });
    }

    private RealmResults<Movie> getActionMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Action")
                .greaterThanOrEqualTo("contentRating", 12)
                .findAllSorted("releaseDate", Sort.DESCENDING);
    }

    private RealmResults<Movie> getComedyMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Komödie")
                .findAllSorted("releaseDate", Sort.DESCENDING);
    }

    private RealmResults<Movie> getThrillerMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Thriller")
                .or()
                .contains("genres", "Horror")
                .findAllSorted("releaseDate", Sort.DESCENDING);
    }

    private RealmResults<Movie> getKidsMovies() {
        return mRealm.where(Movie.class)
                .contains("genres", "Animation")
                .or()
                .contains("genres", "Familie")
                .lessThanOrEqualTo("contentRating", 6)
                .findAllSorted("releaseDate", Sort.DESCENDING);
    }

    private RealmResults<Movie> getMoviesWithExtra(String extra) {
        return mRealm.where(Movie.class)
                .contains("extras", extra)
                .findAllSorted("releaseDate", Sort.DESCENDING);
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
