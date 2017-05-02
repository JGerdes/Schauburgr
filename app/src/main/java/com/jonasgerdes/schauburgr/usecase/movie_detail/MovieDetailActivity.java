package com.jonasgerdes.schauburgr.usecase.movie_detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;
import com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list.ScreeningListAdapter;
import com.jonasgerdes.schauburgr.usecase.movie_detail.screening_list.ScreeningSelectedListener;
import com.jonasgerdes.schauburgr.util.ChromeCustomTabWrapper;
import com.jonasgerdes.schauburgr.util.GlideBitmapReadyListener;
import com.jonasgerdes.schauburgr.util.StringUtil;
import com.jonasgerdes.schauburgr.view.SwipeBackLayout;
import com.jonasgerdes.schauburgr.view.behavior.NestedScrollViewBehavior;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class MovieDetailActivity extends AppCompatActivity
        implements MovieDetailContract.View, SwipeBackLayout.SwipeListener,
        ScreeningSelectedListener {

    /**
     * maximum delay (in ms) to wait for finish loading until activity is opened without transition
     */
    public static final int MAX_DELAY_FOR_TRANSITION = 500;

    @BindView(R.id.swipe_back_layout)
    SwipeBackLayout mSwipeBackLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.title)
    TextView mTitleView;

    @BindView(R.id.genre)
    TextView mGenreView;

    @BindView(R.id.duration)
    TextView mDurationView;

    @BindView(R.id.contentRating)
    TextView mContentRating;

    @BindView(R.id.director)
    TextView mDirector;

    @BindView(R.id.cast)
    TextView mCast;

    @BindView(R.id.description)
    TextView mDescriptionView;

    @BindView(R.id.screeningList)
    RecyclerView mScreeningList;

    @BindView(R.id.next_screenings_title)
    TextView mNextScreeningsTitle;

    @BindView(R.id.poster)
    ImageView mPosterView;

    @BindView(R.id.cover)
    ImageView mCoverView;

    @BindView(R.id.loading_indicator)
    ImageView mLoadingIndicator;

    @Inject
    UrlProvider mUrlProvider;

    @Inject
    ChromeCustomTabWrapper mChromeTab;

    @InjectExtra
    String movieId;

    private MovieDetailContract.Presenter mPresenter;
    private ScreeningListAdapter mScreeningsAdapter;

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle("");

        App.getAppComponent().inject(this);
        ButterKnife.bind(this);
        Dart.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSwipeBackLayout.setSwipeListener(this);

        fixNestedScrollFlingBehavior();

        initScreeningList();

        new MovieDetailPresenter().attachView(this);

        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.RIGHT);

        getWindow().setEnterTransition(slide);
        postponeEnterTransition();

        //if loading takes longer then 500ms, screw shared element transition and start anyway
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startPostponedEnterTransition();
            }
        }, MAX_DELAY_FOR_TRANSITION);
    }

    private void fixNestedScrollFlingBehavior() {
        AppBarLayout appBarLayout = ButterKnife.findById(this, R.id.appBarLayout);
        CoordinatorLayout.LayoutParams params
                = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(new NestedScrollViewBehavior());
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
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.onStartWithMovieId(movieId);
    }

    @Override
    public void showMovie(Movie movie) {
        mTitleView.setText(movie.getTitle());
        mGenreView.setText(StringUtil.concat(movie.getGenres(), ", "));
        mDurationView.setText(movie.getDuration() + " Min");
        @ColorInt int color = getContentRatingColor(this, movie.getContentRating());
        mContentRating.setBackgroundTintList(ColorStateList.valueOf(color));
        mContentRating.setText("ab " + movie.getContentRating());
        if (movie.getDirectors().isEmpty()) {
            mDirector.setVisibility(View.GONE);
        } else {
            mDirector.setText(StringUtil.concat(movie.getDirectors(), ", "));
        }

        if (movie.getCast().isEmpty()) {
            mCast.setVisibility(View.GONE);
        } else {
            mCast.setText(StringUtil.concat(movie.getCast(), ", "));
        }

        mDescriptionView.setText(Html.fromHtml(movie.getDescription()));

        ((AnimatedVectorDrawable) mLoadingIndicator.getDrawable()).start();

        Glide.with(this)
                .load(mUrlProvider.getPosterImageUrl(movie))
                .asBitmap()
                .error(R.drawable.no_network_poster)
                .listener(new GlideBitmapReadyListener() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        Palette palette = Palette.from(bitmap).generate();
                        applyColors(palette);
                        mLoadingIndicator.setVisibility(View.GONE);
                        //start transition
                        startPostponedEnterTransition();
                    }
                })
                .into(mPosterView);


        if(movie.getCoverUrl() != null) {
            Glide.with(this)
                    .load(movie.getCoverUrl())
                    .into(mCoverView);
        }
    }

    @Override
    public void showScreenings(RealmResults<Screening> screenings) {
        mScreeningsAdapter.setScreenings(screenings);
        //hide "next screenings" title if there are no next screenings
        mNextScreeningsTitle.setVisibility(screenings.size() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void openWebpage(String url) {
        mChromeTab.open(this, url);
    }

    private void initScreeningList() {
        mScreeningList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mScreeningsAdapter = new ScreeningListAdapter();
        mScreeningsAdapter.setScreeningSelectedListener(this);
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

    @Override
    public void onFullSwipeBack() {
        finishAfterTransition();
    }

    @Override
    public void onSwipe(float progress) {
        //ignore
    }

    @Override
    public void onScreeningSelected(Screening screening) {
        mPresenter.onScreeningSelected(screening);
    }

    private
    @ColorInt
    int getContentRatingColor(Context context, int contentRating) {
        @ColorRes int colorRes;
        switch (contentRating) {
            case 6:
                colorRes = R.color.colorContentRating6;
                break;
            case 12:
                colorRes = R.color.colorContentRating12;
                break;
            case 16:
                colorRes = R.color.colorContentRating16;
                break;
            case 18:
                colorRes = R.color.colorContentRating18;
                break;
            default:
                colorRes = R.color.colorContentRating0;
                break;
        }
        return ContextCompat.getColor(context, colorRes);
    }

    public static void start(Activity activity, Movie movie, ImageView posterThumbnail) {
        Intent detailsIntent = Henson.with(activity)
                .gotoMovieDetailActivity()
                .movieId(movie.getResourceId())
                .build();
        String transitionName = activity.getString(R.string.transition_movie_poster);
        posterThumbnail.setTransitionName(transitionName);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(activity, posterThumbnail, transitionName);
        activity.startActivity(detailsIntent, options.toBundle());
    }
}
