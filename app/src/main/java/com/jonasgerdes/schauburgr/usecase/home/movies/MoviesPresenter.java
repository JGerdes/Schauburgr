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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jonas on 05.03.2017.
 */

public class MoviesPresenter implements MoviesContract.Presenter {
    @Inject
    SchauburgApi mApi;


    private MoviesContract.View mView;
    private Call<Guide> mPendingCall;

    public MoviesPresenter(MoviesContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadMovies() {
        mPendingCall = mApi.getFullGuide();
        mPendingCall.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                List<Movie> movies = response.body().getMovies();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(movies);
                realm.commitTransaction();
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

    @Override
    public void stop() {
        if (mPendingCall != null) {
            mPendingCall.cancel();
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        mView.openDetails(movie);
    }
}
