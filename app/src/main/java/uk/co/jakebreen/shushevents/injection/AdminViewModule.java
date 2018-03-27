package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.AdminInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.AdminInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.AdminPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.AdminPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AdminViewModule {
    @Provides
    public AdminInteractor provideInteractor() {
        return new AdminInteractorImpl();
    }

    @Provides
    public PresenterFactory<AdminPresenter> providePresenterFactory(@NonNull final AdminInteractor interactor) {
        return new PresenterFactory<AdminPresenter>() {
            @NonNull
            @Override
            public AdminPresenter create() {
                return new AdminPresenterImpl(interactor);
            }
        };
    }
}
