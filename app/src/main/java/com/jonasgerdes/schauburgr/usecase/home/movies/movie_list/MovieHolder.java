package com.jonasgerdes.schauburgr.usecase.home.movies.movie_list;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;
import com.jonasgerdes.schauburgr.network.image.ImageUrlCreator;
import com.jonasgerdes.schauburgr.util.StringUtil;
import com.jonasgerdes.schauburgr.util.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class MovieHolder extends RecyclerView.ViewHolder {

    @Inject
    ImageUrlCreator mImageUrlCreator;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.genre)
    TextView mGenre;

    @BindView(R.id.contentRating)
    TextView mContentRating;

    @BindView(R.id.duration)
    TextView mDuration;

    @BindView(R.id.label3d)
    TextView mLabel3D;

    @BindView(R.id.labelAtmos)
    TextView mLabelAtmos;

    @BindView(R.id.labelOT)
    TextView mLabelOT;

    @BindView(R.id.labelReel)
    TextView mLabelReel;

    @BindView(R.id.poster)
    ImageView mPoster;

    public MovieHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        App.getAppComponent().inject(this);
    }

    public void onBind(Movie movie) {
        Context context = itemView.getContext();
        Resources resources = context.getResources();
        mTitle.setText(movie.getTitle());
        mDuration.setText(movie.getDuration() + " Min");
        mContentRating.setText("ab " + movie.getContentRating());
        @ColorInt int color = getContentRatingColor(context, movie.getContentRating());
        mContentRating.setBackgroundTintList(ColorStateList.valueOf(color));

        ViewUtils.setVisible(mLabel3D, movie.is3D());
        ViewUtils.setVisible(mLabelAtmos, movie.isAtmos());
        ViewUtils.setVisible(mLabelOT, movie.isOT());
        ViewUtils.setVisible(mLabelReel, movie.isReel());

        String genreString = "";
        if (!movie.getGenres().isEmpty()) {
            genreString = StringUtil.concat(movie.getGenres(), ", ");
        }
        mGenre.setText(genreString);

        String posterImageUrl = mImageUrlCreator.getPosterImageUrl(movie);
        Glide.with(context)
                .load(posterImageUrl)
                .error(R.drawable.no_network_poster)
                .centerCrop()
                .into(mPoster);

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

    public ImageView getPosterView() {
        return mPoster;
    }
}
