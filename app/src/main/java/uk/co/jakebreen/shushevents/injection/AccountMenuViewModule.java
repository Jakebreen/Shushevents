package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.AccountMenuInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.AccountMenuInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.AccountMenuPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.AccountMenuPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AccountMenuViewModule {
    @Provides
    public AccountMenuInteractor provideInteractor() {
        return new AccountMenuInteractorImpl();
    }

    @Provides
    public PresenterFactory<AccountMenuPresenter> providePresenterFactory(@NonNull final AccountMenuInteractor interactor) {
        return new PresenterFactory<AccountMenuPresenter>() {
            @NonNull
            @Override
            public AccountMenuPresenter create() {
                return new AccountMenuPresenterImpl(interactor);
            }
        };
    }
}
