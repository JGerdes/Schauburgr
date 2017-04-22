package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
    @Inject
    SchauburgApi mApi;

    @Inject
    Realm mRealm;

    private MoviesContract.View mView;
    private Call<Guide> mPendingCall;

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
        /*RealmResults<Movie> movies = mRealm.where(Movie.class).findAll();
        mView.addMovieCategory(R.string.movie_list_category_top, movies);
        RealmResults<Movie> newest = mRealm.where(Movie.class)
                .isNotNull("releaseDate")
                .lessThan("releaseDate", new LocalDate().minusDays(10).toDate())
                .findAllSorted("releaseDate");
        mView.addMovieCategory(R.string.movie_list_category_new, newest);*/

        RealmResults<Movie> action = mRealm.where(Movie.class)
                .contains("genres", "Action")
                .greaterThanOrEqualTo("contentRating", 12)
                .findAllSorted("releaseDate", Sort.DESCENDING);
        mView.addMovieCategory(R.string.movie_list_category_action, action);

        RealmResults<Movie> comedy = mRealm.where(Movie.class)
                .contains("genres", "Komödie")
                .findAllSorted("releaseDate", Sort.DESCENDING);
        mView.addMovieCategory(R.string.movie_list_category_comedy, comedy);

        RealmResults<Movie> thriller = mRealm.where(Movie.class)
                .contains("genres", "Thriller")
                .or()
                .contains("genres", "Horror")
                .findAllSorted("releaseDate", Sort.DESCENDING);
        mView.addMovieCategory(R.string.movie_list_category_thriller, thriller);

        RealmResults<Movie> kids = mRealm.where(Movie.class)
                .contains("genres", "Animation")
                .or()
                .contains("genres", "Familie")
                .lessThanOrEqualTo("contentRating", 6)
                .findAllSorted("releaseDate", Sort.DESCENDING);
        mView.addMovieCategory(R.string.movie_list_category_kids, kids);

        RealmResults<Movie> d3 = mRealm.where(Movie.class)
                .contains("extras", Movie.EXTRA_3D)
                .findAllSorted("releaseDate", Sort.DESCENDING);
        mView.addMovieCategory(R.string.movie_list_category_3d, d3);

        RealmResults<Movie> specials = mRealm.where(Movie.class)
                .contains("genres", "Met Opera")
                .or()
                .contains("extras", Movie.EXTRA_REEL)
                .or()
                .contains("extras", Movie.EXTRA_TIP)
                .or()
                .contains("extras", Movie.EXTRA_OT)
                .findAllSorted("releaseDate", Sort.DESCENDING);
        mView.addMovieCategory(R.string.movie_list_category_specials, specials);


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
}
