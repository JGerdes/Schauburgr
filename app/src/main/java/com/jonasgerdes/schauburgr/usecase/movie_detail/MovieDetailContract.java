package com.jonasgerdes.schauburgr.usecase.movie_detail;

import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public interface MovieDetailContract {
    interface View extends BaseView<MovieDetailContract.Presenter> {

    }

    interface Presenter extends BasePresenter {
    }
}