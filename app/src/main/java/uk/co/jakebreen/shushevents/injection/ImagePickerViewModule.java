package uk.co.jakebreen.shushevents.injection;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.interactor.ImagePickerInteractor;
import uk.co.jakebreen.shushevents.interactor.impl.ImagePickerInteractorImpl;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.ImagePickerPresenter;
import uk.co.jakebreen.shushevents.presenter.impl.ImagePickerPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ImagePickerViewModule {
    @Provides
    public ImagePickerInteractor provideInteractor() {
        return new ImagePickerInteractorImpl();
    }

    @Provides
    public PresenterFactory<ImagePickerPresenter> providePresenterFactory(@NonNull final ImagePickerInteractor interactor) {
        return new PresenterFactory<ImagePickerPresenter>() {
            @NonNull
            @Override
            public ImagePickerPresenter create() {
                return new ImagePickerPresenterImpl(interactor);
            }
        };
    }
}
