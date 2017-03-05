package com.jonasgerdes.schauburgr.usecase.home.movies;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.model.Guide;
import com.jonasgerdes.schauburgr.network.SchauburgApi;

import javax.inject.Inject;

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

    public MoviesPresenter(MoviesContract.View view) {
        App.getAppComponent().inject(this);
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadMovies() {
        mApi.getFullGuide().enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                mView.showMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                // TODO: 05.03.2017 handle
            }
        });
    }
}
