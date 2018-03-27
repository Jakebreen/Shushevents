package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.SignupActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = SignupViewModule.class)
public interface SignupViewComponent {
    void inject(SignupActivity activity);
}