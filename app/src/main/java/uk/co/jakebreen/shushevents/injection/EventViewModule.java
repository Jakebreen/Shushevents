package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.EventInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.EventInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.EventPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.EventPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class EventViewModule {
    @Provides
    public EventInteractor provideInteractor() {
        return new EventInteractorImpl();
    }

    @Provides
    public PresenterFactory<EventPresenter> providePresenterFactory(@NonNull final EventInteractor interactor) {
        return new PresenterFactory<EventPresenter>() {
            @NonNull
            @Override
            public EventPresenter create() {
                return new EventPresenterImpl(interactor);
            }
        };
    }
}
