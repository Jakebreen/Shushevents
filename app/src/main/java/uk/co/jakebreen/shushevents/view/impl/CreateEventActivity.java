package uk.co.jakebreen.shushevents.view.impl;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Instructor;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.data.remote.APIService;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.CreateEventViewModule;
import uk.co.jakebreen.shushevents.injection.DaggerCreateEventViewComponent;
import uk.co.jakebreen.shushevents.presenter.CreateEventPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.CreateEventView;

public final class CreateEventActivity extends BaseActivity<CreateEventPresenter, CreateEventView> implements CreateEventView, DatePickerDialog.OnDateSetListener, OnMapReadyCallback {
    @Inject
    PresenterFactory<CreateEventPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.et_createEventTitle)
    EditText etCreateEventTitle;
    @BindView(R.id.et_createEventDescription)
    EditText etCreateEventDescription;
    @BindView(R.id.spn_createEventInstructor)
    Spinner spnCreateEventInstructor;
    @BindView(R.id.et_createEventDatePicker)
    EditText etCreateEventDatePicker;
    @BindView(R.id.et_createEventTimePicker)
    EditText etCreateEventTimePicker;
    @BindView(R.id.et_createEventTicketPrice)
    EditText etCreateEventTicketPrice;
    @BindView(R.id.et_createEventTicketMax)
    EditText etCreateEventTicketMax;
    @BindView(R.id.et_createEventVenue)
    EditText etCreateEventVenue;
    @BindView(R.id.spn_createEventDuration)
    Spinner spnCreateEventDuration;
    @BindView(R.id.cb_createEventPublish)
    CheckBox cbCreateEventPublish;
    @BindView(R.id.iv_createEventImageHolder)
    ImageView iv_createEventImageHolder;
    @BindView(R.id.spn_repeatWeeks)
    Spinner spnCreateEventRepeatWeek;
    @BindView(R.id.btn_createEventPublish)
    Button btnCreateEventPublish;

    protected FirebaseUser user;
    private int iTicketMax, iVenue;
    private DatePickerDialog datePickerDialog;
    private Dialog dialog;
    private GoogleMap mMap;
    private Double lat, lng;
    private EditText etCreateVenueTitle, etCreateVenueAddress, etCreateVenueTown, etCreateVenuePostcode;
    private Spinner spnVenue;
    private Venue mVenue;
    private Instructor mInstructor;

    private APIService mImageAPIService;
    private Uri resultUri;

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getInstructors();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

        Calendar calendar = Calendar.getInstance(Locale.UK);
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(
                this, this, startYear, startMonth, startDay);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_venue_picker);

        etCreateVenueTitle = (EditText) dialog.findViewById(R.id.et_createVenueTitle);
        etCreateVenueAddress = (EditText) dialog.findViewById(R.id.et_createVenueAddress);
        etCreateVenueTown = (EditText) dialog.findViewById(R.id.et_createVenueTown);
        etCreateVenuePostcode = (EditText) dialog.findViewById(R.id.et_createVenuePostcode);
        spnVenue = (Spinner) dialog.findViewById(R.id.spn_venueList);

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCreateEventViewComponent.builder()
                .appComponent(parentComponent)
                .createEventViewModule(new CreateEventViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<CreateEventPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }


    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_createEventPublish)
    public void onClickPublish() {
        String title = etCreateEventTitle.getText().toString();
        String description = etCreateEventDescription.getText().toString();
        String instructor;
        if (spnCreateEventInstructor.getSelectedItem() == null) {
            instructor = "";
        } else {
            instructor = spnCreateEventInstructor.getSelectedItem().toString();
        }
        String date = etCreateEventDatePicker.getText().toString();
        String time = etCreateEventTimePicker.getText().toString();
        String ticketPrice = etCreateEventTicketPrice.getText().toString();
        String ticketMax = etCreateEventTicketMax.getText().toString();
        //String venue = etCreateEventVenue.getText().toString();
        String duration = spnCreateEventDuration.getSelectedItem().toString();
        String repeatWeeks = spnCreateEventRepeatWeek.getSelectedItem().toString();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        mInstructor = (Instructor) spnCreateEventInstructor.getSelectedItem();

        if (mPresenter.validateForm(title, description, instructor, date, time, ticketPrice, ticketMax, mVenue, duration, resultUri)) {
            iTicketMax = Integer.parseInt(ticketMax);
            mPresenter.sendEvent(userid, title, description, mInstructor.getUserid(), date, time, ticketPrice, iTicketMax, mVenue.getVenueId(), duration, repeatWeeks, resultUri);
        }
    }

    @OnTouch(R.id.et_createEventDatePicker)
    public boolean onTouchSetDate(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            datePickerDialog.show();
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {
        etCreateEventDatePicker.setText(mPresenter.onDateSetFormatDate(startYear, startMonth, startDay));
    }

    @OnTouch(R.id.et_createEventTimePicker)
    public boolean onTouchSetTime(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            final Calendar myCalender = Calendar.getInstance();
            int hour = myCalender.get(Calendar.HOUR_OF_DAY);
            int minute = myCalender.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                    if (view.isShown()) {
                        myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalender.set(Calendar.MINUTE, minuteOfHour);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String time = sdf.format(myCalender.getTime());

                        etCreateEventTimePicker.setText(time);
                    }
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
            timePickerDialog.setTitle("Choose hour and minute:");
            timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            timePickerDialog.show();
        }

        return false;
    }

    @Override
    public void clearForm() {
        finish();
    }

    @OnTouch(R.id.et_createEventVenue)
    public boolean onTouchVenueDialog(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dialog.setCancelable(false);

            mPresenter.getVenues();

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            Button btnSelectVenue = (Button) dialog.findViewById(R.id.btn_selectVenue);
            Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
            Button btnCreateVenue = (Button) dialog.findViewById(R.id.btn_create);
            final EditText etPostcode = (EditText) dialog.findViewById(R.id.et_createVenuePostcode);

            etPostcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String postcode = String.valueOf(etPostcode.getText());
                    textChangeGetLocation(postcode);
                }
            });

            btnSelectVenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVenue = (Venue) spnVenue.getSelectedItem();
                    //null pointer below
                    etCreateEventVenue.setText(mVenue.getHandle());
                    closeDialog();
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog();
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
                        closeDialog();
                    }
                }
            });

            dialog.show();
        }
        return false;
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

    @Override
    public void closeDialog() {
        etCreateVenueTitle.setText("");
        etCreateVenueAddress.setText("");
        etCreateVenueTown.setText("");
        etCreateVenuePostcode.setText("");
        lat = null;
        lng = null;
        dialog.dismiss();
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
        spnVenue.setAdapter(adapter);
    }

    @Override
    public void displayInstructorSpinner(List<Instructor> instructorList) {
        ArrayAdapter<Instructor> adapter = new ArrayAdapter<Instructor>(this, android.R.layout.simple_list_item_1, android.R.id.text1, instructorList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Instructor instructor = getItem(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setText(instructor.getFirstname() + " " + instructor.getSurname());

                return view;
            }
        };
        spnCreateEventInstructor.setAdapter(adapter);
    }

    @OnClick(R.id.iv_createEventImageHolder)
    public void onClickGetImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(330,60)
                .setMaxCropResultSize(330,60)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                iv_createEventImageHolder.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
