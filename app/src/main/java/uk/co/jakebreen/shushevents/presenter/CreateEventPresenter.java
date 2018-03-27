package uk.co.jakebreen.shushevents.presenter;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.view.CreateEventView;

public interface CreateEventPresenter extends BasePresenter<CreateEventView> {

    boolean validateForm(String title, String description, String instructor, String date,
                         String time, String ticketPrice, String ticketMax, Venue venue,
                         String duration);
    void sendEvent(String userid, String title, String description, String instructorId, String date,
                   String time, String ticketPrice, int ticketMax, int venueId,
                   String duration, String repeatWeeks);
    boolean validateForm(String handle, String address, String town, String postcode, Double lat, Double lng);
    void sendVenue(String handle, String address, String town, String postcode, Double lat, Double lng);
    void getVenues();
    void getInstructors();
    LatLng getLocation(String postcode, Context context);
    String onDateSetFormatDate(int startYear, int startMonth, int startDay);
}