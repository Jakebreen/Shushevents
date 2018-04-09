package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.CancelEventInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.CancelEventInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.CancelEventPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.CancelEventPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class CancelEventViewModule {
    @Provides
    public CancelEventInteractor provideInteractor() {
        return new CancelEventInteractorImpl();
    }

    @Provides
    public PresenterFactory<CancelEventPresenter> providePresenterFactory(@NonNull final CancelEventInteractor interactor) {
        return new PresenterFactory<CancelEventPresenter>() {
            @NonNull
            @Override
            public CancelEventPresenter create() {
                return new CancelEventPresenterImpl(interactor);
            }
        };
    }
}
