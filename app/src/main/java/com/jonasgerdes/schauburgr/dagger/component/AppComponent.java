package com.jonasgerdes.schauburgr.dagger.component;

import com.jonasgerdes.schauburgr.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
}
