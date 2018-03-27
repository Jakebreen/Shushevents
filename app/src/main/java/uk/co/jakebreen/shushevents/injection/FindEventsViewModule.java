package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.FindEventsInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.FindEventsInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.FindEventsPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.FindEventsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class FindEventsViewModule {
    @Provides
    public FindEventsInteractor provideInteractor() {
        return new FindEventsInteractorImpl();
    }

    @Provides
    public PresenterFactory<FindEventsPresenter> providePresenterFactory(@NonNull final FindEventsInteractor interactor) {
        return new PresenterFactory<FindEventsPresenter>() {
            @NonNull
            @Override
            public FindEventsPresenter create() {
                return new FindEventsPresenterImpl(interactor);
            }
        };
    }
}
