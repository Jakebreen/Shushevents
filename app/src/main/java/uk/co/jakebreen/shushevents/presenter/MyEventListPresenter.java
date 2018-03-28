package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.MyEventListView;

public interface MyEventListPresenter extends BasePresenter<MyEventListView> {

    void getEvents(String userid);

}