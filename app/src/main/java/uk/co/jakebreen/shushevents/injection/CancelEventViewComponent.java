package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.CancelEventActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = CancelEventViewModule.class)
public interface CancelEventViewComponent {
    void inject(CancelEventActivity activity);
}