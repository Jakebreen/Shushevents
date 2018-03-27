package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.CreateEventInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.CreateEventInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.CreateEventPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.CreateEventPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class CreateEventViewModule {
    @Provides
    public CreateEventInteractor provideInteractor() {
        return new CreateEventInteractorImpl();
    }

    @Provides
    public PresenterFactory<CreateEventPresenter> providePresenterFactory(@NonNull final CreateEventInteractor interactor) {
        return new PresenterFactory<CreateEventPresenter>() {
            @NonNull
            @Override
            public CreateEventPresenter create() {
                return new CreateEventPresenterImpl(interactor);
            }
        };
    }
}
