package com.jonasgerdes.schauburgr.usecase.home;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.model.MovieRepository;
import com.jonasgerdes.schauburgr.usecase.home.about.AboutView;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuideView;
import com.jonasgerdes.schauburgr.usecase.home.movies.MoviesView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @Inject
    MovieRepository mMovieRepository;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> showView(item.getItemId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //don't show navigation when instance is restored
        if (savedInstanceState == null) {
            showView(R.id.navigation_guide);
        }
    }

    public void navigateTo(@IdRes int id) {
        mNavigation.setSelectedItemId(id);
    }

    private boolean showView(@IdRes int id) {
        String tag = String.valueOf(id);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            switch (id) {
                case R.id.navigation_guide:
                    fragment = GuideView.newInstance();
                    break;
                case R.id.navigation_movies:
                    fragment = MoviesView.newInstance();
                    break;
                case R.id.navigation_about:
                    fragment = AboutView.newInstance();
                    break;
            }
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        mMovieRepository.dispose();
        super.onDestroy();
    }
}
