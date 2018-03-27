package uk.co.jakebreen.shushevents.presenter.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import uk.co.jakebreen.shushevents.interactor.FindEventsInteractor;
import uk.co.jakebreen.shushevents.presenter.FindEventsPresenter;
import uk.co.jakebreen.shushevents.view.FindEventsView;

public final class FindEventsPresenterImpl extends BasePresenterImpl<FindEventsView> implements FindEventsPresenter {

    /**
     * The interactor
     */
    @NonNull
    private final FindEventsInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public FindEventsPresenterImpl(@NonNull FindEventsInteractor interactor) {
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

    @Override
    public void getClasses() {

    }

    @Override
    public boolean checkTownPostcode(String townPostcode) {
        townPostcode.replaceAll("/[^A-Za-z0-9 ]/", "");

        if (townPostcode.isEmpty()) {
            mView.showToast("Town/Postcode field is empty");
        } else {
            return true;
        }

        return false;
    }

    public Pair<Double, Double> getLatLng(String townPostcode) {
        try {
            Geocoder geocoder = new Geocoder((Context) mView);
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(townPostcode, 1);

            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                return new Pair<>(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mView.showToast("Cant find you, please try another address");
        return new Pair<>(0.0, 0.0);
    }

}