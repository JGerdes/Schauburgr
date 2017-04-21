package com.jonasgerdes.schauburgr;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;

import com.jonasgerdes.schauburgr.dagger.component.AppComponent;
import com.jonasgerdes.schauburgr.dagger.component.DaggerAppComponent;
import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;
import com.jonasgerdes.schauburgr.util.ChromeCustomTabWrapper;

import de.jonasrottmann.realmbrowser.RealmBrowser;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {

    private static AppComponent sAppComponent;

    private ChromeCustomTabWrapper mChromeTab = new ChromeCustomTabWrapper();

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule(BuildConfig.SERVER_BASE_URL))
                .build();

        initRealmDb();
        initChromeCustomTabs();

        RealmBrowser.addFilesShortcut(getApplicationContext());
    }

    private void initRealmDb() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void initChromeCustomTabs() {
        CustomTabsClient.bindCustomTabsService(this, "com.android.chrome",
                new CustomTabsServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {}

                    @Override
                    public void onCustomTabsServiceConnected(ComponentName name,
                                                             CustomTabsClient client) {
                        mChromeTab.setClient(client);
                    }
                });
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChromeCustomTabWrapper getChromeTab() {
        return mChromeTab;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

}
