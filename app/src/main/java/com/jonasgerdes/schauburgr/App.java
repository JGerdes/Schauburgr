package com.jonasgerdes.schauburgr;

import android.app.Application;

import com.jonasgerdes.schauburgr.dagger.component.AppComponent;
import com.jonasgerdes.schauburgr.dagger.component.DaggerAppComponent;
import com.jonasgerdes.schauburgr.dagger.module.AppModule;


public class App extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }


    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

}
