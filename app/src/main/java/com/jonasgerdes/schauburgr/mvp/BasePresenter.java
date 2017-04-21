package com.jonasgerdes.schauburgr.mvp;

/**
 * Base interface for all presenter in this app
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}
