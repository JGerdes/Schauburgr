package com.jonasgerdes.schauburgr.dagger.module;

import android.content.res.Resources;

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

    @Provides
    @Singleton
    Resources provideRessources() {
        return mApplication.getResources();
    }
}
