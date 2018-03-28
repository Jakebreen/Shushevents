package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.MyEventListInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.MyEventListInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.MyEventListPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.MyEventListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class MyEventListViewModule {
    @Provides
    public MyEventListInteractor provideInteractor() {
        return new MyEventListInteractorImpl();
    }

    @Provides
    public PresenterFactory<MyEventListPresenter> providePresenterFactory(@NonNull final MyEventListInteractor interactor) {
        return new PresenterFactory<MyEventListPresenter>() {
            @NonNull
            @Override
            public MyEventListPresenter create() {
                return new MyEventListPresenterImpl(interactor);
            }
        };
    }
}
