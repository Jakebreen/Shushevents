package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.view.EventslistView;

public interface EventslistPresenter extends BasePresenter<EventslistView> {

    String getTown(Double lat, Double lng);
    void getEvents(Double lat, Double lng);

    Account getInstructor(String userid);
    Venue getVenue(int venueid);

}