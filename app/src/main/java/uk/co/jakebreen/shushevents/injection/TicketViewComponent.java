package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.TicketActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = TicketViewModule.class)
public interface TicketViewComponent {
    void inject(TicketActivity activity);
}