package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.ResetPassword;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ResetPasswordViewModule.class)
public interface ResetPasswordViewComponent {
    void inject(ResetPassword activity);
}