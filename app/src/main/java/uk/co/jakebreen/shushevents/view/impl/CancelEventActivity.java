package uk.co.jakebreen.shushevents.view.impl;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTouch;
import cz.msebera.android.httpclient.Header;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.CancelEventViewModule;
import uk.co.jakebreen.shushevents.injection.DaggerCancelEventViewComponent;
import uk.co.jakebreen.shushevents.presenter.CancelEventPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.CancelEventView;

public final class CancelEventActivity extends BaseActivity<CancelEventPresenter, CancelEventView> implements CancelEventView, DatePickerDialog.OnDateSetListener {

    private String TAG = CancelEventActivity.class.getSimpleName();

    private static final String PATH_TO_SERVER = "http://www.jakebreen.co.uk/android/shushevents/braintreepayments/index.php";
    private static final int BRAINTREE_REQUEST_CODE = 4949;

    @Inject
    PresenterFactory<CancelEventPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.et_cancelEventDatePicker)
    EditText etCancelEventDatePicker;
    @BindView(R.id.lv_cancelEventList)
    ListView lvCancelEventList;

    private DatePickerDialog datePickerDialog;
    private String clientToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_event);

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
    protected void onStart() {
        super.onStart();

        getClientTokenFromServer();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCancelEventViewComponent.builder()
                .appComponent(parentComponent)
                .cancelEventViewModule(new CancelEventViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<CancelEventPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnTouch(R.id.et_cancelEventDatePicker)
    public boolean onTouchSetDate(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            datePickerDialog.show();
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int startYear, int startMonth, int startDay) {
        etCancelEventDatePicker.setText(mPresenter.onDateSetFormatDate(startYear, startMonth, startDay));
        lvCancelEventList.setAdapter(null);
        mPresenter.getEvents(etCancelEventDatePicker.getText().toString());
    }

    @Override
    public void displayEventList(List<Event> eventlist) {
        ArrayList<Event> arrayListEvent = new ArrayList<Event>(eventlist);
        // Create the adapter to convert the array to views
        EventAdapter adapter = new EventAdapter(this, arrayListEvent);
        // Attach the adapter to a ListView
        lvCancelEventList.setAdapter(adapter);
    }

    public class EventAdapter extends ArrayAdapter<Event> {

        public EventAdapter(Context context, ArrayList<Event> arrayListEvent) {
            super(context, 0, arrayListEvent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Event event = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cancel_event_list, parent, false);
            }
            // Lookup view for data population
            TextView tvEventTitle = (TextView) convertView.findViewById(R.id.tv_eventTitle);
            TextView tvEventDate = (TextView) convertView.findViewById(R.id.tv_eventDate);
            TextView tvEventTime = (TextView) convertView.findViewById(R.id.tv_eventTime);
            TextView tvEventTicketPrice = (TextView) convertView.findViewById(R.id.tv_eventTicketPrice);
            TextView tvEventTicketRemaining = (TextView) convertView.findViewById(R.id.tv_eventTicketRemaining);
            TextView tvEventDistanceMiles = (TextView) convertView.findViewById(R.id.tv_eventDistanceMiles);
            TextView tvEventEntrants = (TextView) convertView.findViewById(R.id.tv_eventTicketEntrants);
            TextView tvEventInstructorName = (TextView) convertView.findViewById(R.id.tv_eventInstructorName);
            TextView tvEventVenueHandle = (TextView) convertView.findViewById(R.id.tv_eventVenueHandle);

            String formattedDistance = String.format("%.02f", event.getDistance());

            // Populate the data into the template view using the data object
            tvEventTitle.setText(event.getTitle());
            tvEventDate.setText(event.getDate());
            tvEventTime.setText(event.getTime());
            tvEventTicketPrice.setText(event.getEntryFee());
            tvEventEntrants.setText("(" +String.valueOf(event.getEntrants()) + " attendees)");
            tvEventInstructorName.setText("Instructor: " + event.getInstructorFirstname() + " " + event.getInstructorSurname());
            tvEventVenueHandle.setText("Venue: " + event.getVenueHandle());

            int remainingTickets = event.getMaxTickets() - event.getEntrants();
            //Log.i(TAG, "ticketsX " + String.valueOf(remainingTickets) + " - " + event.getDate() + " - " + event.getIdevent());
            //Log.i(TAG, "ticketsX " + String.valueOf(remainingTickets) + " - " + event.getEntrants() + "/" + event.getMaxTickets() + ": " + event.getIdevent() + " - " + event.getDate());

            if (remainingTickets <= 0) {
                tvEventTicketRemaining.setText("Fully booked");
                //tvEventTicketRemaining.setTextColor(Color.RED);
                tvEventTicketRemaining.setTypeface(null, Typeface.BOLD_ITALIC);
            } else if (remainingTickets == 1) {
                tvEventTicketRemaining.setText(remainingTickets + " ticket left");
                //tvEventTicketRemaining.setTextColor(Color.parseColor("#808080"));
                tvEventTicketRemaining.setTypeface(null, Typeface.BOLD);
            } else if (remainingTickets > 1 && remainingTickets <= 5) {
                tvEventTicketRemaining.setText(remainingTickets + " tickets left");
                //tvEventTicketRemaining.setTextColor(Color.parseColor("#808080"));
                tvEventTicketRemaining.setTypeface(null, Typeface.BOLD);
            } else if (remainingTickets > 5) {
                tvEventTicketRemaining.setText("Tickets available");
                //tvEventTicketRemaining.setTextColor(Color.parseColor("#808080"));
                tvEventTicketRemaining.setTypeface(null, Typeface.BOLD);
            }

            // Return the completed view to render on screen

            lvCancelEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    lvCancelEventList.setItemChecked(position, true);
                    lvCancelEventList.setSelector(R.color.offWhite);

                    final Event selectedEvent = (Event) lvCancelEventList.getItemAtPosition(position);

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(CancelEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
                    } else {
                        builder = new AlertDialog.Builder(CancelEventActivity.this);
                    }
                    builder.setTitle("Cancel and refund")
                            .setMessage("Are you sure you want to cancel this class and refund all attendees?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.cancelEvent(selectedEvent.getIdevent());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return convertView;
        }
    }

    //Braintree
    @Override
    public void getClientTokenFromServer() {
        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.get(PATH_TO_SERVER, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "Client token: " + getString(R.string.token_failed) + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseToken) {
                Log.d(TAG, "Client token: " + responseToken);
                clientToken = responseToken;
            }
        });
    }
}
