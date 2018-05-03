package uk.co.jakebreen.shushevents.presenter;

import android.util.Pair;

import uk.co.jakebreen.shushevents.view.FindEventsView;

public interface FindEventsPresenter extends BasePresenter<FindEventsView> {

    void getClasses();
    boolean checkTownPostcode(String townPostcode);
    Pair<Double, Double> getLatLng(String townPostcode);
    void openWebsite();

}