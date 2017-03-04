package com.jonasgerdes.schauburgr.dagger.component;

import com.jonasgerdes.schauburgr.dagger.module.AppModule;
import com.jonasgerdes.schauburgr.dagger.module.DataModule;
import com.jonasgerdes.schauburgr.usecase.home.guide.GuidePresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(GuidePresenter presenter);
}
