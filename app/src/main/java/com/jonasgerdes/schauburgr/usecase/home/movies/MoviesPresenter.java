package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
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
        RealmResults<Movie> movies = mRealm.where(Movie.class).findAll();
        mView.showMovies(movies);
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
