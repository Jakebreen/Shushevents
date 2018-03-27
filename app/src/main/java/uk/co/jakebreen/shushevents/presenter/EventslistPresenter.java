package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.EventslistView;

public interface EventslistPresenter extends BasePresenter<EventslistView> {

    String getTown(Double lat, Double lng);
    void getEvents(Double lat, Double lng);

}