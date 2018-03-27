package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.EventslistInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.EventslistInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.EventslistPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.EventslistPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class EventslistViewModule {
    @Provides
    public EventslistInteractor provideInteractor() {
        return new EventslistInteractorImpl();
    }

    @Provides
    public PresenterFactory<EventslistPresenter> providePresenterFactory(@NonNull final EventslistInteractor interactor) {
        return new PresenterFactory<EventslistPresenter>() {
            @NonNull
            @Override
            public EventslistPresenter create() {
                return new EventslistPresenterImpl(interactor);
            }
        };
    }
}
