package com.jonasgerdes.schauburgr.util;

import android.support.customtabs.CustomTabsClient;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 21.04.2017
 */

public class ChromeCustomTabWrapper {

    CustomTabsClient mClient;

    public void warmup() {
        if (mClient != null) {
            mClient.warmup(0);
        }
    }

    public void setClient(CustomTabsClient client) {
        mClient = client;
    }
}
