package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.LoginActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = LoginViewModule.class)
public interface LoginViewComponent {
    void inject(LoginActivity activity);
}