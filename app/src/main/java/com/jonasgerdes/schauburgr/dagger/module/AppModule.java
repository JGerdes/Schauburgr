package com.jonasgerdes.schauburgr.dagger.module;

import android.content.res.Resources;

import com.jonasgerdes.schauburgr.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module providing app related dependencies like the app instance itself of resources
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

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
