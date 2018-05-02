package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.VenuePickerInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.VenuePickerInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.VenuePickerPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.VenuePickerPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class VenuePickerViewModule {
    @Provides
    public VenuePickerInteractor provideInteractor() {
        return new VenuePickerInteractorImpl();
    }

    @Provides
    public PresenterFactory<VenuePickerPresenter> providePresenterFactory(@NonNull final VenuePickerInteractor interactor) {
        return new PresenterFactory<VenuePickerPresenter>() {
            @NonNull
            @Override
            public VenuePickerPresenter create() {
                return new VenuePickerPresenterImpl(interactor);
            }
        };
    }
}
