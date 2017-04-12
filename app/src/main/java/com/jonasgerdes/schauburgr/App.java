package com.jonasgerdes.schauburgr;

import android.app.Application;
import android.content.pm.PackageManager;

import com.jonasgerdes.schauburgr.dagger.component.AppComponent;
import com.jonasgerdes.schauburgr.dagger.component.DaggerAppComponent;
import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule(BuildConfig.SERVER_BASE_URL))
                .build();

        initRealmDb();

        RealmBrowser.addFilesShortcut(getApplicationContext());
    }

    private void initRealmDb() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

}
