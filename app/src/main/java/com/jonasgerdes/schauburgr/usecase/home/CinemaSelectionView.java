package com.jonasgerdes.schauburgr.usecase.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.SchauburgUrlProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 16-Nov-17
 */

public class CinemaSelectionView extends FrameLayout {

    PublishSubject<String> mSelectionSubject = PublishSubject.create();

    public CinemaSelectionView(@NonNull Context context) {
        super(context);
        inflateLayout();
    }

    private void inflateLayout() {
        LayoutInflater.from(getContext()).inflate(
                R.layout.home_cinema_selection_dialog,
                this
        );
        ButterKnife.bind(this);
    }

    public void dismiss() {
        mSelectionSubject.onError(new Throwable("Nothing selected"));
    }

    public Single<String> selections() {
        return mSelectionSubject.firstOrError();
    }

    @OnClick(R.id.cinema_schauburg)
    void onSchauburgSelected() {
        mSelectionSubject.onNext(SchauburgUrlProvider.CinemaHost.SCHAUBURG_CINEWORLD);

    }
    @OnClick(R.id.cinema_central)
    void onCentralSelected() {
        mSelectionSubject.onNext(SchauburgUrlProvider.CinemaHost.CENTRAL_CINEWORLD);
    }
}
