package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

@UiThread
public interface FindEventsView {

    void showToast(String message);

}