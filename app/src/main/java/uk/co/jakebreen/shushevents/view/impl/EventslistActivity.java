package uk.co.jakebreen.shushevents.view.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerEventslistViewComponent;
import uk.co.jakebreen.shushevents.injection.EventslistViewModule;
import uk.co.jakebreen.shushevents.presenter.EventslistPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.EventslistView;

public final class EventslistActivity extends BaseActivity<EventslistPresenter, EventslistView> implements EventslistView {

    private String TAG = EventslistActivity.class.getSimpleName();

    @Inject
    PresenterFactory<EventslistPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.tv_town)
    TextView tvTown;
    @BindView(R.id.ll_changeLocation)
    LinearLayout llChangeLocation;
    @BindView(R.id.lv_eventList)
    ListView lvEventList;

    private String myTown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventslist);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerEventslistViewComponent.builder()
                .appComponent(parentComponent)
                .eventslistViewModule(new EventslistViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<EventslistPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get lat lng from prefs or move to previous activity
        Double myLat = getDefaultsDouble("myLat");
        Double myLng = getDefaultsDouble("myLng");

        if (!myLat.equals(0.0) && !myLng.equals(0.0) && !myLat.equals(null) && !myLng.equals(null)) {
            myTown = mPresenter.getTown(myLat, myLng);
            tvTown.setText(myTown);
            mPresenter.getEvents(myLat, myLng);
        } else {
            finish();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.ll_changeLocation)
    public void onClickChangeLocation() {
        returnToFindEvent();
    }

    @Override
    public void returnToFindEvent() {
        removeDefault("myLat");
        removeDefault("myLng");
        removeDefault("myTown");

        //Intent intent = new Intent(this, FindEventsActivity.class);
        //startActivity(intent);
        finish();
    }

    @Override
    public void displayEventList(List<Event> eventlist) {
        // Construct the data source
        ArrayList<Event> arrayListEvent = new ArrayList<Event>(eventlist);
        // Create the adapter to convert the array to views
        EventAdapter adapter = new EventAdapter(this, arrayListEvent);
        // Attach the adapter to a ListView
        lvEventList.setAdapter(adapter);
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event_list, parent, false);
            }
            // Lookup view for data population
            TextView tvEventTitle = (TextView) convertView.findViewById(R.id.tv_eventTitle);
            TextView tvEventDate = (TextView) convertView.findViewById(R.id.tv_eventDate);
            TextView tvEventTime = (TextView) convertView.findViewById(R.id.tv_eventTime);
            TextView tvEventTicketPrice = (TextView) convertView.findViewById(R.id.tv_eventTicketPrice);
            TextView tvEventTicketRemaining = (TextView) convertView.findViewById(R.id.tv_eventTicketRemaining);
            TextView tvEventDistanceMiles = (TextView) convertView.findViewById(R.id.tv_eventDistanceMiles);

            String formattedDistance = String.format("%.02f", event.getDistance());

            // Populate the data into the template view using the data object
            tvEventTitle.setText(event.getTitle());
            tvEventDate.setText(event.getDate());
            tvEventTime.setText(event.getTime());
            tvEventTicketPrice.setText(event.getEntryFee());
            //tvEventTicketRemaining.setText(event.getUserid());

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

            tvEventDistanceMiles.setText(formattedDistance + "mi");

            // Return the completed view to render on screen

            lvEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    lvEventList.setItemChecked(position, true);
                    lvEventList.setSelector(R.color.offWhite);

                    Event selectedEvent = (Event) lvEventList.getItemAtPosition(position);

                    Intent intent = new Intent(EventslistActivity.this, EventActivity.class);
                    intent.putExtra("event", (Serializable) selectedEvent);
                    startActivity(intent);

                    //mPresenter.getRoles();
                }
            });

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeDefault("myLat");
        removeDefault("myLng");
        removeDefault("myTown");

        //Intent intent = new Intent(this, FindEventsActivity.class);
        //startActivity(intent);
        finish();
    }
}
