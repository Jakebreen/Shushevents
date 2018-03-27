package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.SplashInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.SplashInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.SplashPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.SplashPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class SplashViewModule {
    @Provides
    public SplashInteractor provideInteractor() {
        return new SplashInteractorImpl();
    }

    @Provides
    public PresenterFactory<SplashPresenter> providePresenterFactory(@NonNull final SplashInteractor interactor) {
        return new PresenterFactory<SplashPresenter>() {
            @NonNull
            @Override
            public SplashPresenter create() {
                return new SplashPresenterImpl(interactor);
            }
        };
    }
}
