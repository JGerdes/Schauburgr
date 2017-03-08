package com.jonasgerdes.schauburgr.dagger.module;

import com.jonasgerdes.schauburgr.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    App mApplication;

    public AppModule(App mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    App provideApplication() {
        return mApplication;
    }
}
