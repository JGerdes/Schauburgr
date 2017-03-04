package com.jonasgerdes.schauburgr.usecase.home;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jonasgerdes.schauburgr.R;

import java.util.List;

import butterknife.BindViews;

public class HomeActivity extends AppCompatActivity {

    @BindViews({R.id.navigation_guide})
    List<HomeView> mUseCaseViews;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return showView(item.getItemId());
        }

    };

    private boolean showView(@IdRes int id) {
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
        for (HomeView useCaseView : mUseCaseViews) {
            if (useCaseView.getId() == id) {
                useCaseView.onStart();
            } else {
                useCaseView.onStop();
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showView(R.id.navigation_guide);
    }

}
