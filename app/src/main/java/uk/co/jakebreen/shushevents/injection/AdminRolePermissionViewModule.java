package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.AdminRolePermissionInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.AdminRolePermissionInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.AdminRolePermissionPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.AdminRolePermissionPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AdminRolePermissionViewModule {
    @Provides
    public AdminRolePermissionInteractor provideInteractor() {
        return new AdminRolePermissionInteractorImpl();
    }

    @Provides
    public PresenterFactory<AdminRolePermissionPresenter> providePresenterFactory(@NonNull final AdminRolePermissionInteractor interactor) {
        return new PresenterFactory<AdminRolePermissionPresenter>() {
            @NonNull
            @Override
            public AdminRolePermissionPresenter create() {
                return new AdminRolePermissionPresenterImpl(interactor);
            }
        };
    }
}
