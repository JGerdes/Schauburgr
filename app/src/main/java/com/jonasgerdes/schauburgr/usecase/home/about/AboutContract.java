package com.jonasgerdes.schauburgr.usecase.home.about;

import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

/**
 * Created by jonas on 08.03.2017.
 */

public interface AboutContract {
    interface View extends BaseView<AboutContract.Presenter> {


    }

    interface Presenter extends BasePresenter {

    }
}