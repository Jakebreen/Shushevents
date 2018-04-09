package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.UpdateEventActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = UpdateEventViewModule.class)
public interface UpdateEventViewComponent {
    void inject(UpdateEventActivity activity);
}