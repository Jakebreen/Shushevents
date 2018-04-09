package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.List;

import uk.co.jakebreen.shushevents.data.model.Event;

@UiThread
public interface CancelEventView {

    void showToast(String message);
    void displayEventList(List<Event> eventlist);
    void getClientTokenFromServer();

}