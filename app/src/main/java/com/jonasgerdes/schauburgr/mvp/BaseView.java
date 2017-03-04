package com.jonasgerdes.schauburgr.mvp;


public interface BaseView<T> {
    void setPresenter(T presenter);
}
