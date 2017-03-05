package com.jonasgerdes.schauburgr;

import android.app.Application;

import com.jonasgerdes.schauburgr.dagger.component.AppComponent;
import com.jonasgerdes.schauburgr.dagger.component.DaggerAppComponent;
import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;


public class App extends Application {

    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule(BuildConfig.SERVER_BASE_URL))
                .build();
    }


    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

}
