package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.CreateEventActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = CreateEventViewModule.class)
public interface CreateEventViewComponent {
    void inject(CreateEventActivity activity);
}