package uk.co.jakebreen.shushevents.presenter;

import java.math.BigDecimal;

import uk.co.jakebreen.shushevents.view.EventTicketOrderView;

public interface EventTicketOrderPresenter extends BasePresenter<EventTicketOrderView> {

    BigDecimal calculateTicketTotal(int selectedTicketTotal, BigDecimal entryFee);
    void sendTicket(String userid, int idevent, String txid, String txDateTime, int entrants);
}