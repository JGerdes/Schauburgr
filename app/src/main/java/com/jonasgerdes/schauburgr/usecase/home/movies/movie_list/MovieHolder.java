package com.jonasgerdes.schauburgr.usecase.home.movies.movie_list;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.UrlProvider;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.util.GlideListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class MovieHolder extends RecyclerView.ViewHolder {

    @Inject
    UrlProvider mUrlProvider;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.poster)
    ImageView mPoster;

    @BindView(R.id.extra_ribbon)
    TextView mExtraRibbon;

    @BindView(R.id.loading_indicator)
    ImageView mLoadingIndicator;

    public MovieHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        App.getAppComponent().inject(this);
    }

    public void onBind(Movie movie) {
        Context context = itemView.getContext();
        mTitle.setText(movie.getTitle());

        mLoadingIndicator.setImageResource(R.drawable.anim_loading_rotation_white_24dp);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        ((AnimatedVectorDrawable) mLoadingIndicator.getDrawable()).start();
        initExtraRibbon(mExtraRibbon, movie);

        String posterImageUrl = mUrlProvider.getPosterImageUrl(movie);
        Glide.with(context)
                .load(posterImageUrl)
                .asBitmap()
                .centerCrop()
                .listener(new GlideListener(
                        bitmap -> mLoadingIndicator.setVisibility(View.GONE),
                        exception -> mLoadingIndicator
                                .setImageResource(R.drawable.ic_signal_wifi_off_white_24dp)))
                .into(mPoster);
    }

    private void initExtraRibbon(TextView extraBadge, Movie movie) {
        extraBadge.setVisibility(View.VISIBLE);
        if (movie.getExtras().contains(Movie.EXTRA_LAST_SCREENINGS)) {
            extraBadge.setText(R.string.movie_extra_last_screening);
            return;
        }
        if (movie.getExtras().contains(Movie.EXTRA_PREVIEW)) {
            extraBadge.setText(R.string.movie_extra_preview);
            return;
        }
        if (movie.getExtras().contains(Movie.EXTRA_TIP)) {
            extraBadge.setText(R.string.movie_extra_tip);
            return;
        }
        if (movie.getExtras().contains(Movie.EXTRA_REEL)) {
            extraBadge.setText(R.string.movie_extra_reel);
            return;
        }
        if (movie.getExtras().contains(Movie.EXTRA_LADIES_NIGHT)) {
            extraBadge.setText(R.string.movie_extra_ladies_night);
            return;
        }
        extraBadge.setVisibility(View.GONE);
    }


    public ImageView getPosterView() {
        return mPoster;
    }
}
