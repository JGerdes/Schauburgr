package com.jonasgerdes.schauburgr.usecase.movie_detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
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
import com.jonasgerdes.schauburgr.util.ColorUtil;
import com.jonasgerdes.schauburgr.util.GlideListener;
import com.jonasgerdes.schauburgr.util.StringUtil;
import com.jonasgerdes.schauburgr.util.ViewUtils;
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
        ScreeningSelectedListener, NestedScrollView.OnScrollChangeListener {

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

    @BindView(R.id.trailer_button)
    ImageView mTrailerButton;

    @BindView(R.id.loading_indicator)
    ImageView mLoadingIndicator;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView mScrollView;

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

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mScrollView.setOnScrollChangeListener(this);

        mSwipeBackLayout.setSwipeListener(this);

        fixNestedScrollFlingBehavior();

        initScreeningList();

        new MovieDetailPresenter().attachView(this);

        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.RIGHT);

        getWindow().setEnterTransition(slide);
        postponeEnterTransition();

        //if loading takes longer then 500ms, screw shared element transition and start anyway
        new Handler().postDelayed(() -> startPostponedEnterTransition(), MAX_DELAY_FOR_TRANSITION);
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

        ViewUtils.loadAnimationAndStart(mLoadingIndicator,
                R.drawable.anim_loading_rotation_white_24dp);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(mUrlProvider.getPosterImageUrl(movie))
                .asBitmap()
                .listener(new GlideListener(bitmap -> {
                    Palette palette = Palette.from(bitmap).generate();
                    applyColors(palette);
                    mLoadingIndicator.setVisibility(View.GONE);
                    //start transition
                    startPostponedEnterTransition();
                }, exception -> {
                    mLoadingIndicator.setImageResource(R.drawable.ic_signal_wifi_off_white_24dp);
                    //start transition
                    startPostponedEnterTransition();
                }))
                .into(mPosterView);


        if (movie.getCoverUrl() != null) {
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

    @Override
    public void displayTrailerLink() {
        mTrailerButton.setVisibility(View.VISIBLE);
        mTrailerButton.setOnClickListener(v -> mPresenter.onTrailerLinkClicked());
    }

    @Override
    public void enableCoverScrim(boolean enabled) {
        if (enabled) {
            mTrailerButton.setBackgroundResource(R.color.colorBackgroundDark50);
        } else {
            mTrailerButton.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
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

        int darkColor = palette.getDarkVibrantColor(
                palette.getDominantColor(defaultColor)
        );
        darkColor = ColorUtil.maxBrightness(darkColor, 0.6f);
        getWindow().setStatusBarColor(darkColor);
        getWindow().setNavigationBarColor(darkColor);
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

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX,
                               int scrollY, int oldScrollX, int oldScrollY) {
        if (mTitleView.getTop() + mTitleView.getHeight() / 2 < scrollY) {
            mCollapsingToolbarLayout.setTitle(mTitleView.getText());
        } else {
            mCollapsingToolbarLayout.setTitle(null);
        }
    }
}
