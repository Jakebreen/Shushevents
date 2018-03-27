package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Venue;

@UiThread
public interface EventView {

    void showToast(String message);
    void populateData();
    void populateInstructorData(Account instructor);
    void populateVenueData(Venue venue);
    boolean checkAvailability();
}