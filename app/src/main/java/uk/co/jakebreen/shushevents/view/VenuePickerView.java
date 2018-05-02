package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.List;

import uk.co.jakebreen.shushevents.data.model.Venue;

@UiThread
public interface VenuePickerView {

    void showToast(String message);
    void displayVenueSpinner(List<Venue> venueList);
    void textChangeGetLocation(String postcode);

}