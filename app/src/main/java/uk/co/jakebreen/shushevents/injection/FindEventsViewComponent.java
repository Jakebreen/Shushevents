package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.FindEventsActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = FindEventsViewModule.class)
public interface FindEventsViewComponent {
    void inject(FindEventsActivity activity);
}