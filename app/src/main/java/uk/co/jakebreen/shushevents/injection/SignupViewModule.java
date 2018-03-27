package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.SignupInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.SignupInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.SignupPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.SignupPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class SignupViewModule {
    @Provides
    public SignupInteractor provideInteractor() {
        return new SignupInteractorImpl();
    }

    @Provides
    public PresenterFactory<SignupPresenter> providePresenterFactory(@NonNull final SignupInteractor interactor) {
        return new PresenterFactory<SignupPresenter>() {
            @NonNull
            @Override
            public SignupPresenter create() {
                return new SignupPresenterImpl(interactor);
            }
        };
    }
}
