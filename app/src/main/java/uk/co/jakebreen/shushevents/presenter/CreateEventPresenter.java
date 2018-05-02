package uk.co.jakebreen.shushevents.presenter;

import android.net.Uri;

import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.view.CreateEventView;

public interface CreateEventPresenter extends BasePresenter<CreateEventView> {

    boolean validateForm(String title, String description, String instructor, String date,
                         String time, String ticketPrice, String ticketMax, Venue venue,
                         String duration, Uri uri, String selectedCoverImage);
    void sendEvent(String userid, String title, String description, String instructorId, String date,
                   String time, String ticketPrice, int ticketMax, int venueId,
                   String duration, String repeatWeeks, Uri uri, String selectedCoverImage);
    void getInstructors();
    String onDateSetFormatDate(int startYear, int startMonth, int startDay);
    void sendCoverImage(Uri uri);
}