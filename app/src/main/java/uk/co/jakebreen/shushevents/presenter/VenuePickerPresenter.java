package uk.co.jakebreen.shushevents.presenter;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import uk.co.jakebreen.shushevents.view.VenuePickerView;

public interface VenuePickerPresenter extends BasePresenter<VenuePickerView> {

    void getVenues();
    boolean validateForm(String handle, String address, String town, String postcode, Double lat, Double lng);
    void sendVenue(String handle, String address, String town, String postcode, Double lat, Double lng);
    LatLng getLocation(String postcode, Context context);

}