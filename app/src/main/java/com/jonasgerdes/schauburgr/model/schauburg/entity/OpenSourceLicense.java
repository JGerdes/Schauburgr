package com.jonasgerdes.schauburgr.model.schauburg.entity;

import android.support.annotation.StringRes;

/**
 * Model representation of a open source license to be shown in "about"-section of app.
 * Holds string resource identifier for both title and body of a license
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.03.2017
 */

public class OpenSourceLicense {

    /**
     * String resource of the title of the library license is for
     */
    @StringRes
    private int title;

    /**
     * String resource of details of the license containing author of library, year, title of the
     * license and the actual license text
     */
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
