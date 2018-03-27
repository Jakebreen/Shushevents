package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;

import uk.co.jakebreen.shushevents.presenter.AccountMenuPresenter;
import uk.co.jakebreen.shushevents.view.AccountMenuView;
import uk.co.jakebreen.shushevents.interactor.AccountMenuInteractor;

import javax.inject.Inject;

public final class AccountMenuPresenterImpl extends BasePresenterImpl<AccountMenuView> implements AccountMenuPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final AccountMenuInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public AccountMenuPresenterImpl(@NonNull AccountMenuInteractor interactor) {
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