package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.List;

import uk.co.jakebreen.shushevents.data.model.Ticket;

@UiThread
public interface MyEventListView {

    void showToast(String message);
    void displayEventList(List<Ticket> eventlist);

}