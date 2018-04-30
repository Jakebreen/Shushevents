package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakebreen.shushevents.data.model.Instructor;
import uk.co.jakebreen.shushevents.data.model.Venue;

@UiThread
public interface CreateEventView {
    void showToast(String message);
    void textChangeGetLocation(String postcode);
    void closeDialog();
    void displayVenueSpinner(List<Venue> venueList);
    void clearForm();
    void displayInstructorSpinner(List<Instructor> instructorList);
    void cropImageTask();
    void displayCoverImageList(ArrayList<String> coverImageList);
}