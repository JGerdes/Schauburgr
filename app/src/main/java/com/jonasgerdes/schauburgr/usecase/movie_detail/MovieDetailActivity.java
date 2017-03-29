package com.jonasgerdes.schauburgr.usecase.movie_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;

import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
        RealmChangeListener<Movie> {

    @InjectExtra
    String movieId;

    private Realm mRealm;
    private MovieDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Dart.inject(this);
        mRealm = Realm.getDefaultInstance();
        new MovieDetailPresenter(this);

        Movie movie = mRealm.where(Movie.class)
                .equalTo("resourceId", movieId)
                .findFirst();

        movie.addChangeListener(this);
        onChange(movie);

    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Override
    public void onChange(Movie movie) {
        setTitle(movie.getTitle());
    }
}
