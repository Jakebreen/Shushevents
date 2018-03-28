package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.TicketInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.TicketInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.TicketPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.TicketPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class TicketViewModule {
    @Provides
    public TicketInteractor provideInteractor() {
        return new TicketInteractorImpl();
    }

    @Provides
    public PresenterFactory<TicketPresenter> providePresenterFactory(@NonNull final TicketInteractor interactor) {
        return new PresenterFactory<TicketPresenter>() {
            @NonNull
            @Override
            public TicketPresenter create() {
                return new TicketPresenterImpl(interactor);
            }
        };
    }
}
