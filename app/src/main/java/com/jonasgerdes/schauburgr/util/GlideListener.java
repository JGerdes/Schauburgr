package com.jonasgerdes.schauburgr.util;

import android.graphics.Bitmap;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Enables using Glide callbacks ({@link com.bumptech.glide.BitmapRequestBuilder#listener(RequestListener)})
 * with lambda expressions
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 29.03.2017
 */

public class GlideListener implements RequestListener<String, Bitmap> {

    public interface ExceptionListener {
        void onException(Exception exception);
    }

    public interface ResourceListener {
        void onResourceReady(Bitmap bitmap);
    }

    private ResourceListener mResourceListener;
    private ExceptionListener mExceptionListener;

    public GlideListener(ResourceListener resourceListener) {
        mResourceListener = resourceListener;
    }

    public GlideListener(ResourceListener resourceListener,
                         ExceptionListener exceptionListener) {
        mResourceListener = resourceListener;
        mExceptionListener = exceptionListener;
    }

    @Override
    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean
            isFirstResource) {
        if (mExceptionListener != null) {
            mExceptionListener.onException(e);
        }
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean
            isFromMemoryCache, boolean isFirstResource) {
        if (mResourceListener != null) {
            mResourceListener.onResourceReady(resource);
        }
        return false;
    }

}
