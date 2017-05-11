package com.jonasgerdes.schauburgr.dagger.module;

import android.content.res.Resources;

import com.jonasgerdes.schauburgr.App;
import com.jonasgerdes.schauburgr.util.ChromeCustomTabWrapper;

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
    private App mApplication;
    private ChromeCustomTabWrapper mChromeTab;

    public AppModule(App application, ChromeCustomTabWrapper chromeTab) {
        mApplication = application;
        mChromeTab = chromeTab;
    }

    @Provides
    @Singleton
    App provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return mApplication.getResources();
    }

    @Provides
    @Singleton
    ChromeCustomTabWrapper provideChromeCustomTab() {
        return mChromeTab;
    }
}
