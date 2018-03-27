package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.ResetPasswordInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.ResetPasswordInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.ResetPasswordPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.ResetPasswordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ResetPasswordViewModule {
    @Provides
    public ResetPasswordInteractor provideInteractor() {
        return new ResetPasswordInteractorImpl();
    }

    @Provides
    public PresenterFactory<ResetPasswordPresenter> providePresenterFactory(@NonNull final ResetPasswordInteractor interactor) {
        return new PresenterFactory<ResetPasswordPresenter>() {
            @NonNull
            @Override
            public ResetPasswordPresenter create() {
                return new ResetPasswordPresenterImpl(interactor);
            }
        };
    }
}
