package uk.co.jakebreen.shushevents.injection;

import android.content.Context;

import uk.co.jakebreen.shushevents.App;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context getAppContext();

    App getApp();
}