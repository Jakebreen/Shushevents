package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.math.BigDecimal;

@UiThread
public interface EventTicketOrderView {

    void showToast(String message);
    void displayTotal(int selectedTicketTotal, BigDecimal entryFee);
    void getClientTokenFromServer();
    int checkAvailability();

}