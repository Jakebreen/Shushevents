package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.RegisterAccountInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.RegisterAccountInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.RegisterAccountPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.RegisterAccountPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class RegisterAccountViewModule {
    @Provides
    public RegisterAccountInteractor provideInteractor() {
        return new RegisterAccountInteractorImpl();
    }

    @Provides
    public PresenterFactory<RegisterAccountPresenter> providePresenterFactory(@NonNull final RegisterAccountInteractor interactor) {
        return new PresenterFactory<RegisterAccountPresenter>() {
            @NonNull
            @Override
            public RegisterAccountPresenter create() {
                return new RegisterAccountPresenterImpl(interactor);
            }
        };
    }
}
