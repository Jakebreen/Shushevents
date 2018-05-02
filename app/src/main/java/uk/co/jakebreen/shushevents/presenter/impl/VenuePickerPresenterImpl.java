package uk.co.jakebreen.shushevents.presenter.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.interactor.VenuePickerInteractor;
import uk.co.jakebreen.shushevents.presenter.VenuePickerPresenter;
import uk.co.jakebreen.shushevents.view.VenuePickerView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class VenuePickerPresenterImpl extends BasePresenterImpl<VenuePickerView> implements VenuePickerPresenter {

    private String TAG = VenuePickerPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final VenuePickerInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public VenuePickerPresenterImpl(@NonNull VenuePickerInteractor interactor) {
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
    public void getVenues() {
        mAPIService.getVenue().enqueue(new Callback<List<Venue>>() {
            @Override
            public void onResponse(Call<List<Venue>> call, Response<List<Venue>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Venue> venueList = response.body();
                    if (mView != null) mView.displayVenueSpinner(venueList);
                }
            }

            @Override
            public void onFailure(Call<List<Venue>> call, Throwable t) {
                mView.showToast("Unable to find venues");
                showResponse(t.toString());
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }

    @Override
    public boolean validateForm(String handle, String address, String town, String postcode, Double lat, Double lng) {

        if (handle.equals(null) || handle.equals("")) {
            mView.showToast("Venue name field empty");
            return false;
        } else if (address.equals(null) || address.equals("")) {
            mView.showToast("Address field empty");
            return false;
        } else if (town.equals(null) || town.equals("")) {
            mView.showToast("Town field empty");
            return false;
        } else if (postcode.equals(null) || postcode.equals("")) {
            mView.showToast("Postcode field empty");
            return false;
        } else if (lat == null || lng == null || lat == 0.0 || lng == 0.0) {
            mView.showToast("Must place a map marker");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void sendVenue(String handle, String address, String town, String postcode, Double lat, Double lng) {
        mAPIService.saveVenue(handle, address, town, postcode, lat, lng).enqueue(new Callback<Venue>() {
            @Override
            public void onResponse(Call<Venue> call, Response<Venue> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    mView.showToast("New venue created");
                }
            }

            @Override
            public void onFailure(Call<Venue> call, Throwable t) {
                showResponse(t.toString());
                mView.showToast("Error creating new venue");
            }
        });

    }

    @Override
    public LatLng getLocation(String postcode, Context context) {
        LatLng latLng = null;
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            addresses = geocoder.getFromLocationName(postcode, 1);
            if (addresses.size() > 0) {
                latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}