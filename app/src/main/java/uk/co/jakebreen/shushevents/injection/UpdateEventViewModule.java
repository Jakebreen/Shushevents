package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.UpdateEventInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.UpdateEventInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.UpdateEventPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.UpdateEventPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class UpdateEventViewModule {
    @Provides
    public UpdateEventInteractor provideInteractor() {
        return new UpdateEventInteractorImpl();
    }

    @Provides
    public PresenterFactory<UpdateEventPresenter> providePresenterFactory(@NonNull final UpdateEventInteractor interactor) {
        return new PresenterFactory<UpdateEventPresenter>() {
            @NonNull
            @Override
            public UpdateEventPresenter create() {
                return new UpdateEventPresenterImpl(interactor);
            }
        };
    }
}
