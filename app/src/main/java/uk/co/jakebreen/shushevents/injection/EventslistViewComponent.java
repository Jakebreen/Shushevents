package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.EventslistActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = EventslistViewModule.class)
public interface EventslistViewComponent {
    void inject(EventslistActivity activity);
}