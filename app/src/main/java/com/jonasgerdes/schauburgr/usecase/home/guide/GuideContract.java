package com.jonasgerdes.schauburgr.usecase.home.guide;

import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.mvp.BasePresenter;
import com.jonasgerdes.schauburgr.mvp.BaseView;

import java.util.List;

public interface GuideContract {
    interface View extends BaseView<GuideContract.Presenter> {
        void showScreenings(List<Screening> screenings);

    }

    interface Presenter extends BasePresenter {
        void loadProgram();
    }
}