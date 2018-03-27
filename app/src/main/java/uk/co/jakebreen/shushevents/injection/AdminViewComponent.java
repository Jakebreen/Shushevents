package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.AdminActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = AdminViewModule.class)
public interface AdminViewComponent {
    void inject(AdminActivity activity);
}