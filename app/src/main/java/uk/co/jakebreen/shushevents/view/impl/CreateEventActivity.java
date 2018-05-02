package uk.co.jakebreen.shushevents.view.impl;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

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
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.CreateEventViewModule;
import uk.co.jakebreen.shushevents.injection.DaggerCreateEventViewComponent;
import uk.co.jakebreen.shushevents.presenter.CreateEventPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.CreateEventView;

public final class CreateEventActivity extends BaseActivity<CreateEventPresenter, CreateEventView> implements CreateEventView, DatePickerDialog.OnDateSetListener {

    private String TAG = CreateEventActivity.class.getSimpleName();

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
    private GoogleMap mMap;
    private Venue mVenue;
    private Instructor mInstructor;
    private String selectedCoverImage;

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

        Log.i(TAG, "selectedCoverImage: " + selectedCoverImage + " resultUri: " + resultUri);

        if (mPresenter.validateForm(title, description, instructor, date, time, ticketPrice, ticketMax, mVenue, duration, resultUri, selectedCoverImage)) {
            iTicketMax = Integer.parseInt(ticketMax);
            mPresenter.sendEvent(userid, title, description, mInstructor.getUserid(), date, time, ticketPrice, iTicketMax, mVenue.getVenueId(), duration, repeatWeeks, resultUri, selectedCoverImage);
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
            Intent i = new Intent(this, VenuePickerActivity.class);
            startActivityForResult(i, 2);
        }
        return false;
    }

    @OnClick(R.id.iv_createEventImageHolder)
    public void onClickGetImage() {
        Intent i = new Intent(this, ImagePickerActivity.class);
        startActivityForResult(i, 1);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                if (data.getStringExtra("selectImage") != null) {
                    selectedCoverImage = data.getStringExtra("selectImage");
                    Picasso.get().load("http://jakebreen.co.uk/android/shushevents/classimages/" + selectedCoverImage).fit().into(iv_createEventImageHolder);
                } else if (data.getStringExtra("uriResult") != null) {
                    resultUri = Uri.parse(data.getStringExtra("uriResult"));
                    iv_createEventImageHolder.setImageURI(resultUri);
                }

            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mVenue = (Venue) data.getSerializableExtra("venue");
                etCreateEventVenue.setText(mVenue.getHandle());
            }
        }
    }
}
