package com.jonasgerdes.schauburgr.usecase.home;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jonasgerdes.schauburgr.R;
import com.jonasgerdes.schauburgr.usecase.home.about.AboutView;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuideView;
import com.jonasgerdes.schauburgr.usecase.home.movies.MoviesView;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showView(item.getItemId());
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showView(R.id.navigation_guide);
    }

    private boolean showView(@IdRes int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.navigation_guide:
                setTitle(R.string.title_guide);
                fragment = GuideView.newInstance();
                break;
            case R.id.navigation_movies:
                setTitle(R.string.title_movies);
                fragment = MoviesView.newInstance();
                break;
            case R.id.navigation_about:
                setTitle(R.string.title_about);
                fragment = AboutView.newInstance();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment)
                    .commitAllowingStateLoss();
            return true;
        }
        return false;
    }
}
