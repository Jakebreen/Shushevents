package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.SplashActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = SplashViewModule.class)
public interface SplashViewComponent {
    void inject(SplashActivity activity);
}