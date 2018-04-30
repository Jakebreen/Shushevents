package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Ticket;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerTicketViewComponent;
import uk.co.jakebreen.shushevents.injection.TicketViewModule;
import uk.co.jakebreen.shushevents.presenter.TicketPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.TicketView;

public final class TicketActivity extends BaseActivity<TicketPresenter, TicketView> implements TicketView, OnMapReadyCallback {
    @Inject
    PresenterFactory<TicketPresenter> mPresenterFactory;

    private Ticket mTicket;
    private GoogleMap mMap;
    private LatLng latLng;

    TextView tvTicketEventTitle, tvTicketDateTime, tvTicketTicketNum, tvTicketCustomerName,
            tvTicketTxid, tvTicketDateTimePurchase, tvEventPersonsPaid, tvTicketCancelled;
    LinearLayout llShowDirections;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvTicketEventTitle = (TextView) findViewById(R.id.tv_ticketEventTitle);
        tvTicketDateTime = (TextView) findViewById(R.id.tv_ticketDateTime);
        tvTicketTicketNum = (TextView) findViewById(R.id.tv_ticketTicketNum);
        tvTicketCustomerName = (TextView) findViewById(R.id.tv_ticketCustomerName);
        tvTicketTxid = (TextView) findViewById(R.id.tv_ticketTxid);
        tvTicketDateTimePurchase = (TextView) findViewById(R.id.tv_ticketDateTimePurchase);
        tvEventPersonsPaid = (TextView) findViewById(R.id.tv_eventPersonsPaid);
        tvTicketCancelled = (TextView) findViewById(R.id.tv_ticketCancelled);
        llShowDirections = (LinearLayout) findViewById(R.id.ll_showDirections);

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
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        latLng = new LatLng(mTicket.getLat(), mTicket.getLng());

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(mTicket.getHandle()));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @OnClick(R.id.ll_showDirections)
    public void onClickShowDirections() {
        String strUri = "http://maps.google.com/maps?q=loc:" + mTicket.getLat() + "," + mTicket.getLng() + " (" + mTicket.getHandle() + ")";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }
}
