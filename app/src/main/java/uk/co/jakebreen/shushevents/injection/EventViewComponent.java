package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.EventActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = EventViewModule.class)
public interface EventViewComponent {
    void inject(EventActivity activity);
}