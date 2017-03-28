package com.jonasgerdes.schauburgr.usecase.home;

import android.support.annotation.IdRes;

/**
 * Created by jonas on 04.03.2017.
 */

public interface HomeView {
    void onStart();

    void onStop();

    void onDestroy();

    @IdRes int getId();

    void setVisibility(int visibility);


}
