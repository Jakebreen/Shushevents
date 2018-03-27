package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.presenter.SignupPresenter;
import uk.co.jakebreen.shushevents.view.SignupView;
import uk.co.jakebreen.shushevents.interactor.SignupInteractor;

import javax.inject.Inject;

public final class SignupPresenterImpl extends BasePresenterImpl<SignupView> implements SignupPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final SignupInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public SignupPresenterImpl(@NonNull SignupInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }
}