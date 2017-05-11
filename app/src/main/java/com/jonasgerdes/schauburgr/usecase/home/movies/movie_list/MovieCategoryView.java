package com.jonasgerdes.schauburgr.usecase.home.movies.movie_list;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.schauburg.entity.MovieCategory;
import com.jonasgerdes.schauburgr.util.OffsetDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 21.04.2017
 */

public class MovieCategoryView extends FrameLayout {

    @BindView(R.id.root)
    View mRoot;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.subtitle)
    TextView mSubTitle;

    @BindView(R.id.movieList)
    RecyclerView mMovieList;

    private MovieListAdapter mMovieListAdapter;

    public MovieCategoryView(@NonNull Context context) {
        super(context);
        init();
    }

    public MovieCategoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovieCategoryView(@NonNull Context context, @Nullable AttributeSet attrs,
                             @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MovieCategoryView(@NonNull Context context, @Nullable AttributeSet attrs,
                             @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_movie_category, this);
        ButterKnife.bind(this);
        mMovieListAdapter = new MovieListAdapter();
        mMovieList.setAdapter(mMovieListAdapter);
    }

    public void bindCategory(MovieCategory category) {
        mTitle.setText(category.getTitle());
        mMovieListAdapter.setMovies(category.getMovies());
        if (category.getSubTitle() != -1) {
            mSubTitle.setText(category.getSubTitle());
        } else {
            mSubTitle.setVisibility(GONE);
        }

        if (category.getBackground() != -1) {
            mRoot.setBackgroundResource(category.getBackground());
            int offsetX = getContext().getResources()
                    .getDimensionPixelOffset(R.dimen.category_background_offset_x);
            mMovieList.addItemDecoration(new OffsetDecoration(0, offsetX));
        }
    }

    public void setMovieSelectedListener(MovieListAdapter.MovieClickedListener listener) {
        mMovieListAdapter.setMovieClickedListener(listener);
    }
}
