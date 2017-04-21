package com.jonasgerdes.schauburgr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.jonasgerdes.schauburgr.R;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 21.04.2017
 */

public class ChromeCustomTabWrapper {

    private CustomTabsClient mClient;

    public void warmup() {
        if (mClient != null) {
            mClient.warmup(0);
        }
    }

    public void setClient(CustomTabsClient client) {
        mClient = client;
    }

    public void open(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Bitmap icon = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_arrow_back_white_24dp
        );
        builder.setCloseButtonIcon(icon);
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.none);
        builder.setExitAnimations(context, 0, R.anim.slide_out_right);
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
