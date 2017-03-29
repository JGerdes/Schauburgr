package com.jonasgerdes.schauburgr.util;

import android.graphics.Bitmap;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public abstract class GlideBitmapReadyListener implements RequestListener<String, Bitmap> {


    @Override
    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean
            isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean
            isFromMemoryCache, boolean isFirstResource) {
        onBitmapReady(resource);
        return false;
    }

    public abstract void onBitmapReady(Bitmap bitmap);
}
