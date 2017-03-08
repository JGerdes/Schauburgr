package com.jonasgerdes.schauburgr.model;

import android.support.annotation.StringRes;

/**
 * Created by jonas on 08.03.2017.
 */

public class OpenSourceLicense {

    @StringRes
    private int title;

    @StringRes
    private int body;

    public OpenSourceLicense(int title, int body) {
        this.title = title;
        this.body = body;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getBody() {
        return body;
    }

    public void setBody(int body) {
        this.body = body;
    }
}
