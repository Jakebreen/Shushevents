package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import javax.inject.Inject;

import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Ticket;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerTicketViewComponent;
import uk.co.jakebreen.shushevents.injection.TicketViewModule;
import uk.co.jakebreen.shushevents.presenter.TicketPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.TicketView;

public final class TicketActivity extends BaseActivity<TicketPresenter, TicketView> implements TicketView {
    @Inject
    PresenterFactory<TicketPresenter> mPresenterFactory;

    private Ticket mTicket;

    TextView tvTicketEventTitle, tvTicketDateTime, tvTicketTicketNum, tvTicketCustomerName,
            tvTicketTxid, tvTicketDateTimePurchase, tvEventPersonsPaid, tvTicketCancelled;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        tvTicketEventTitle = (TextView) findViewById(R.id.tv_ticketEventTitle);
        tvTicketDateTime = (TextView) findViewById(R.id.tv_ticketDateTime);
        tvTicketTicketNum = (TextView) findViewById(R.id.tv_ticketTicketNum);
        tvTicketCustomerName = (TextView) findViewById(R.id.tv_ticketCustomerName);
        tvTicketTxid = (TextView) findViewById(R.id.tv_ticketTxid);
        tvTicketDateTimePurchase = (TextView) findViewById(R.id.tv_ticketDateTimePurchase);
        tvEventPersonsPaid = (TextView) findViewById(R.id.tv_eventPersonsPaid);
        tvTicketCancelled = (TextView) findViewById(R.id.tv_ticketCancelled);

        Intent intent = getIntent();
        mTicket = (Ticket) intent.getSerializableExtra("ticket");
        populateData();

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerTicketViewComponent.builder()
                .appComponent(parentComponent)
                .ticketViewModule(new TicketViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<TicketPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void populateData() {
        tvTicketEventTitle.setText(mTicket.getTitle());
        tvTicketDateTime.setText(mTicket.getDate() + " - " + mTicket.getTime());
        tvTicketTicketNum.setText(String.valueOf(mTicket.getEntrants()));
        tvTicketCustomerName.setText(mTicket.getFirstname() + " " + mTicket.getSurname());
        tvTicketTxid.setText(mTicket.getTxid());
        tvTicketDateTimePurchase.setText(mTicket.getTxdatetime());
        if (mTicket.getRefunded() == 1) {
            tvTicketCancelled.setText("This class has been cancelled and your ticket has been refunded, apologies for any inconvenience caused.");
        }

    }
}
