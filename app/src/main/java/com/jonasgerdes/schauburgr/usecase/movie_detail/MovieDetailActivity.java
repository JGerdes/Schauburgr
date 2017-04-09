package com.jonasgerdes.schauburgr.usecase.movie_detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.model.Screening;
import com.jonasgerdes.schauburgr.network.image.ImageUrlCreator;
import com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list.ScreeningListAdapter;
import com.jonasgerdes.schauburgr.util.GlideBitmapReadyListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
        RealmChangeListener<Movie> {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.poster)
    ImageView mPosterView;

    @BindView(R.id.description)
    TextView mDescriptionView;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.screeningList)
    RecyclerView mScreeningList;

    @Inject
    ImageUrlCreator mImageUrlCreator;

    @InjectExtra
    String movieId;

    private MovieDetailContract.Presenter mPresenter;
    private ScreeningListAdapter mScreeningsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        App.getAppComponent().inject(this);
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initScreeningList();

        new MovieDetailPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.loadMovie(movieId);
        mPresenter.loadScreeningsFor(movieId);
    }

    @Override
    public void onChange(Movie movie) {
        setTitle(movie.getTitle());
        mDescriptionView.setText(Html.fromHtml(movie.getDescription()));

        Glide.with(this)
                .load(mImageUrlCreator.getPosterImageUrl(movie))
                .asBitmap()
                .listener(new GlideBitmapReadyListener() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        Palette palette = Palette.from(bitmap).generate();
                        applyColors(palette);
                    }
                })
                .into(mPosterView);
    }

    @Override
    public void showMovie(Movie movie) {
        movie.addChangeListener(this);
        onChange(movie);
    }

    @Override
    public void showScreenings(RealmResults<Screening> screenings) {
        mScreeningsAdapter.setScreenings(screenings);
    }

    private void initScreeningList() {
        mScreeningList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mScreeningsAdapter = new ScreeningListAdapter();
        mScreeningList.setAdapter(mScreeningsAdapter);
    }

    private void applyColors(Palette palette) {
        @ColorInt final int defaultColor
                = ContextCompat.getColor(MovieDetailActivity.this, R.color.colorPrimaryDark);
        @ColorInt int background = palette.getVibrantColor(
                palette.getDominantColor(defaultColor)
        );
        mCollapsingToolbarLayout.setBackgroundColor(background);
        mCollapsingToolbarLayout.setContentScrimColor(background);

        getWindow().setStatusBarColor(
                palette.getDarkVibrantColor(
                        palette.getDominantColor(defaultColor)
                )
        );
    }
}
