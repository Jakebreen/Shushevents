package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.CancelEventView;

public interface CancelEventPresenter extends BasePresenter<CancelEventView> {

    String onDateSetFormatDate(int startYear, int startMonth, int startDay);
    void getEvents(String date);
    void cancelEvent(int eventid);

}