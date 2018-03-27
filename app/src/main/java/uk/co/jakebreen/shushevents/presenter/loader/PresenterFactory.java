package uk.co.jakebreen.shushevents.presenter.loader;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.presenter.BasePresenter;

/**
 * Factory to implement to create a presenter
 */
public interface PresenterFactory<T extends BasePresenter> {
    @NonNull
    T create();
}
