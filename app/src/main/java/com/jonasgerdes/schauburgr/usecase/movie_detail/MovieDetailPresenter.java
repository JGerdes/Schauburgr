package com.jonasgerdes.schauburgr.usecase.movie_detail;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {


    private final MovieDetailContract.View mView;

    public MovieDetailPresenter(MovieDetailContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }
}
