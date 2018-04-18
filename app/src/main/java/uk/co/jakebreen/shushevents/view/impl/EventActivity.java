package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerEventViewComponent;
import uk.co.jakebreen.shushevents.injection.EventViewModule;
import uk.co.jakebreen.shushevents.presenter.EventPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.EventView;

public final class EventActivity extends BaseActivity<EventPresenter, EventView> implements EventView, OnMapReadyCallback {

    private String TAG = EventActivity.class.getSimpleName();

    @Inject
    PresenterFactory<EventPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.tv_eventPageTitle)
    TextView tvEventPageTitle;
    @BindView(R.id.btn_joinClass)
    Button btnJoinClass;
    @BindView(R.id.tv_eventPageInstructor)
    TextView tvEventPageInstructor;
    @BindView(R.id.tv_eventPageDuration)
    TextView tvEventPageDuration;
    @BindView(R.id.tv_eventPageTicketFee)
    TextView tvEventPageTicketFee;
    @BindView(R.id.tv_eventPageDate)
    TextView tvEventPageDate;
    @BindView(R.id.tv_eventPageTime)
    TextView tvEventPageTime;
    @BindView(R.id.tv_eventPageAddress)
    TextView tvEventPageAddress;
    @BindView(R.id.tv_eventPageTown)
    TextView tvEventPageTown;
    @BindView(R.id.tv_eventPagePostcode)
    TextView tvEventPagePostcode;
    @BindView(R.id.tv_eventPageDescription)
    TextView tvEventPageDescription;

    private Event mEvent;
    private Venue mVenue;
    private GoogleMap mMap;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerEventViewComponent.builder()
                .appComponent(parentComponent)
                .eventViewModule(new EventViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<EventPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnJoinClass.setEnabled(false);

        Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra("event");
        populateData();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean checkAvailability() {
        int remainingTickets = mEvent.getMaxTickets() - mEvent.getEntrants();
        if (remainingTickets <= 0) {
            btnJoinClass.setText("FULLY BOOKED");
            btnJoinClass.setEnabled(false);
            btnJoinClass.setBackgroundColor(Color.parseColor("#808080"));
            return false;
        }
        return true;
    }

    @Override
    public void populateData() {
        tvEventPageTitle.setText(mEvent.getTitle());
        tvEventPageTicketFee.setText(mEvent.getEntryFee());
        tvEventPageDuration.setText(mEvent.getDuration());
        tvEventPageDate .setText(mEvent.getDate());
        tvEventPageTime.setText(mEvent.getTime());
        tvEventPageDescription.setText(mEvent.getDescription());

        mPresenter.getInstructor(mEvent.getInstructorId());
        mPresenter.getVenue(mEvent.getVenueid());
    }

    @Override
    public void populateInstructorData(Account instructor) {
        tvEventPageInstructor.setText(instructor.getFirstname() + " " + instructor.getSurname());
    }

    @Override
    public void populateVenueData(Venue venue) {
        mVenue = venue;

        tvEventPageAddress.setText(venue.getAddress());
        tvEventPageTown.setText(venue.getTown());
        tvEventPagePostcode.setText(venue.getPostcode());

        latLng = new LatLng(venue.getLat(), venue.getLng());

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(venue.getHandle()));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (checkAvailability()) btnJoinClass.setEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng latLng = new LatLng (getDefaultsDouble("myLat"), getDefaultsDouble("myLng"));

        //googleMap.moveCamera(latLng);

        //CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @OnClick(R.id.btn_joinClass)
    public void onClickJoinClass() {
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, EventTicketOrder.class);
            intent.putExtra("event", (Serializable) mEvent);
            intent.putExtra("venue", (Serializable) mVenue);
            startActivity(intent);
        }
    }
}
