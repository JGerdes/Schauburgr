package com.jonasgerdes.schauburgr.usecase.home.movies.movie_list;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class MovieHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.genre)
    TextView mGenre;

    @BindView(R.id.contentRating)
    TextView mContentRating;

    @BindView(R.id.duration)
    TextView mDuration;

    public MovieHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(Movie movie) {
        Context context = itemView.getContext();
        Resources resources = context.getResources();
        mTitle.setText(movie.getTitle());
        mDuration.setText(movie.getDuration() + " Minuten");
        mContentRating.setText("ab " + movie.getContentRating());
        @ColorInt int color = getContentRatingColor(context, movie.getContentRating());
        mContentRating.setBackgroundTintList(ColorStateList.valueOf(color));

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
}
