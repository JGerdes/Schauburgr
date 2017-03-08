package com.jonasgerdes.schauburgr.usecase.home;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.jonasgerdes.schauburgr.R;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindViews({R.id.navigation_guide, R.id.navigation_movies, R.id.navigation_about})
    List<HomeView> mUseCaseViews;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showView(item.getItemId());
        }

    };

    private boolean showView(@IdRes int id) {
        for (HomeView useCaseView : mUseCaseViews) {
            if (useCaseView.getId() == id) {
                useCaseView.onStart();
                useCaseView.setVisibility(View.VISIBLE);
            } else {
                useCaseView.setVisibility(View.GONE);
                useCaseView.onStop();
            }
        }

        switch (id) {
            case R.id.navigation_guide:
                setTitle(R.string.title_guide);
                return true;
            case R.id.navigation_movies:
                setTitle(R.string.title_movies);
                return true;
            case R.id.navigation_about:
                setTitle(R.string.title_about);
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showView(R.id.navigation_guide);
    }

}
