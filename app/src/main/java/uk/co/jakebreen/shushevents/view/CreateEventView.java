package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.List;

import uk.co.jakebreen.shushevents.data.model.Instructor;

@UiThread
public interface CreateEventView {
    void showToast(String message);
    void clearForm();
    void displayInstructorSpinner(List<Instructor> instructorList);
}