package com.jonasgerdes.schauburgr.mvp;

/**
 * Base interface for all (MVP-)views in this app
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.03.2017
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
