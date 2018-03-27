package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.EventTicketOrderInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.EventTicketOrderInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.EventTicketOrderPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.EventTicketOrderPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class EventTicketOrderViewModule {
    @Provides
    public EventTicketOrderInteractor provideInteractor() {
        return new EventTicketOrderInteractorImpl();
    }

    @Provides
    public PresenterFactory<EventTicketOrderPresenter> providePresenterFactory(@NonNull final EventTicketOrderInteractor interactor) {
        return new PresenterFactory<EventTicketOrderPresenter>() {
            @NonNull
            @Override
            public EventTicketOrderPresenter create() {
                return new EventTicketOrderPresenterImpl(interactor);
            }
        };
    }
}
