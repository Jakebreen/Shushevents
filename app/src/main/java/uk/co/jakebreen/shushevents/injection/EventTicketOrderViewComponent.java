package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.EventTicketOrder;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = EventTicketOrderViewModule.class)
public interface EventTicketOrderViewComponent {
    void inject(EventTicketOrder activity);
}