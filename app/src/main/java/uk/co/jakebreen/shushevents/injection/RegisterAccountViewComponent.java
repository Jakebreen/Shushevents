package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.RegisterAccountActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisterAccountViewModule.class)
public interface RegisterAccountViewComponent {
    void inject(RegisterAccountActivity activity);
}