package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.EventView;

public interface EventPresenter extends BasePresenter<EventView> {

    void getInstructor(String userid);
    void getVenue(int venueid);

}