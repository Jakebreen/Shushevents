package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerVenuePickerViewComponent;
import uk.co.jakebreen.shushevents.injection.VenuePickerViewModule;
import uk.co.jakebreen.shushevents.presenter.VenuePickerPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.VenuePickerView;

public final class VenuePickerActivity extends BaseActivity<VenuePickerPresenter, VenuePickerView> implements VenuePickerView, OnMapReadyCallback {
    @Inject
    PresenterFactory<VenuePickerPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    private Spinner spnVenue;
    private GoogleMap mMap;
    private Double lat, lng;
    private EditText etCreateVenueTitle, etCreateVenueAddress, etCreateVenueTown, etCreateVenuePostcode;
    private Button btnSelectVenue, btnClose, btnCreateVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_picker);

        etCreateVenueTitle = (EditText) findViewById(R.id.et_createVenueTitle);
        etCreateVenueAddress = (EditText) findViewById(R.id.et_createVenueAddress);
        etCreateVenueTown = (EditText) findViewById(R.id.et_createVenueTown);
        etCreateVenuePostcode = (EditText) findViewById(R.id.et_createVenuePostcode);
        spnVenue = (Spinner) findViewById(R.id.spn_venueList);
        btnSelectVenue = (Button) findViewById(R.id.btn_selectVenue);
        btnClose = (Button) findViewById(R.id.btn_close);
        btnCreateVenue = (Button) findViewById(R.id.btn_create);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

        etCreateVenuePostcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String postcode = String.valueOf(etCreateVenuePostcode.getText());
                textChangeGetLocation(postcode);
            }
        });

        btnSelectVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Venue mVenue = (Venue) spnVenue.getSelectedItem();
                Intent intent = new Intent();
                intent.putExtra("venue", mVenue);
                setResult(RESULT_OK, intent);
                finishActivity();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCreateVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String venueTitle = etCreateVenueTitle.getText().toString();
                String venueAddress = etCreateVenueAddress.getText().toString();
                String venueTown = etCreateVenueTown.getText().toString();
                String venuePostcode = etCreateVenuePostcode.getText().toString();

                if (mPresenter.validateForm(venueTitle, venueAddress, venueTown, venuePostcode, lat, lng)) {
                    mPresenter.sendVenue(venueTitle, venueAddress, venueTown, venuePostcode, lat, lng);

                    mPresenter.getVenues();
                    showToast("New venue created, you can now select it from the list");

                    etCreateVenueTitle.setText(null);
                    etCreateVenueAddress.setText(null);
                    etCreateVenueTown.setText(null);
                    etCreateVenuePostcode.setText(null);
                }
            }
        });

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerVenuePickerViewComponent.builder()
                .appComponent(parentComponent)
                .venuePickerViewModule(new VenuePickerViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<VenuePickerPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getVenues();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayVenueSpinner(List<Venue> venueList) {

        ArrayAdapter<Venue> adapter = new ArrayAdapter<Venue>(this, android.R.layout.simple_list_item_1, android.R.id.text1, venueList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Venue venue = getItem(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setText(venue.getHandle());

                return view;
            }
        };
        spnVenue.setAdapter(null);
        adapter.notifyDataSetChanged();
        spnVenue.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                lat = arg0.getPosition().latitude;
                lng = arg0.getPosition().longitude;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });
    }

    @Override
    public void textChangeGetLocation(String postcode) {
        LatLng latLng =  mPresenter.getLocation(postcode, this);

        if (latLng != null) {
            lat = latLng.latitude;
            lng = latLng.longitude;
        }

        if (latLng != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Venue").draggable(true));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void finishActivity() {
        finish();
    }
}
