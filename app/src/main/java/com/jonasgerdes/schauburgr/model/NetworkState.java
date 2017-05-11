package com.jonasgerdes.schauburgr.model;

import android.support.annotation.IntDef;

/**
 * Representation of a state of a network request. Contains additional data for some states like
 * error messages when in state {@link #STATE_ERROR}
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 28.04.2017
 */

public class NetworkState {

    public static final int NO_MESSAGE = -1;

    public static final int STATE_ERROR = -1;
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LOADING = 1;

    public static final NetworkState DEFAULT = new NetworkState(STATE_DEFAULT);
    public static final NetworkState LOADING = new NetworkState(STATE_LOADING);

    @IntDef({STATE_ERROR, STATE_LOADING, STATE_DEFAULT})
    @interface State {
    }

    private
    @State
    int mState;
    private int mHttpStatusCode;
    private String mMessage;
    private int mMessageResource = NO_MESSAGE;

    public NetworkState(@State int state) {
        mState = state;
    }

    public
    @State
    int getState() {
        return mState;
    }

    public int getHttpStatusCode() {
        return mHttpStatusCode;
    }

    public NetworkState setHttpStatusCode(int httpStatusCode) {
        mHttpStatusCode = httpStatusCode;
        return this;
    }

    public String getMessage() {
        return mMessage;
    }

    public NetworkState setMessage(String message) {
        mMessage = message;
        return this;
    }

    public int getMessageResource() {
        return mMessageResource;
    }

    public NetworkState setMessage(int messageResource) {
        mMessageResource = messageResource;
        return this;
    }
}
