package com.jonasgerdes.schauburgr.usecase.home.movies.movie_list;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.Movie;
import com.jonasgerdes.schauburgr.model.UrlProvider;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 05.03.2017.
 */

public class CompactMovieHolder extends RecyclerView.ViewHolder {

    @Inject
    UrlProvider mUrlProvider;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.poster)
    ImageView mPoster;

    @BindView(R.id.loading_indicator)
    ImageView mLoadingIndicator;

    public CompactMovieHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        App.getAppComponent().inject(this);
    }

    public void onBind(Movie movie) {
        Context context = itemView.getContext();
        mTitle.setText(movie.getTitle());

        mLoadingIndicator.setVisibility(View.VISIBLE);
        ((AnimatedVectorDrawable) mLoadingIndicator.getDrawable()).start();

        String posterImageUrl = mUrlProvider.getPosterImageUrl(movie);
        Glide.with(context)
                .load(posterImageUrl)
                .error(R.drawable.no_network_poster)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        mLoadingIndicator.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mPoster);
    }

    public ImageView getPosterView() {
        return mPoster;
    }
}
