package uk.co.jakebreen.shushevents.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Ticket;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerMyEventListViewComponent;
import uk.co.jakebreen.shushevents.injection.MyEventListViewModule;
import uk.co.jakebreen.shushevents.presenter.MyEventListPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.MyEventListView;

public final class MyEventListActivity extends BaseActivity<MyEventListPresenter, MyEventListView> implements MyEventListView {
    @Inject
    PresenterFactory<MyEventListPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.lv_myEventList)
    ListView lvMyEventList;

    protected FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_list);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        mPresenter.getEvents(userid);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMyEventListViewComponent.builder()
                .appComponent(parentComponent)
                .myEventListViewModule(new MyEventListViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<MyEventListPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayEventList(List<Ticket> myEventlist) {
        // Construct the data source
        ArrayList<Ticket> arrayListEvent = new ArrayList<Ticket>(myEventlist);
        // Create the adapter to convert the array to views
        EventAdapter adapter = new EventAdapter(this, arrayListEvent);
        // Attach the adapter to a ListView
        lvMyEventList.setAdapter(adapter);
    }

    public class EventAdapter extends ArrayAdapter<Ticket> {

        public EventAdapter(Context context, ArrayList<Ticket> arrayListEvent) {
            super(context, 0, arrayListEvent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Ticket ticket = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_paid_event_list, parent, false);
            }
            // Lookup view for data population
            TextView tvEventTitle = (TextView) convertView.findViewById(R.id.tv_eventTitle);
            TextView tvEventDate = (TextView) convertView.findViewById(R.id.tv_eventDate);
            TextView tvEventTime = (TextView) convertView.findViewById(R.id.tv_eventTime);
            TextView tvEventTicketPrice = (TextView) convertView.findViewById(R.id.tv_eventTicketPrice);
            TextView tvEventPersonsPaid = (TextView) convertView.findViewById(R.id.tv_eventPersonsPaid);
            TextView tvEventTicketCancelled = (TextView) convertView.findViewById(R.id.tv_eventTicketCancelled);
            ImageView ivEventImage = (ImageView) convertView.findViewById(R.id.iv_eventImage);

            // Populate the data into the template view using the data object
            tvEventTitle.setText(ticket.getTitle());
            tvEventDate.setText(ticket.getDate());
            tvEventTime.setText(ticket.getTime());

            BigDecimal totalFee = new BigDecimal(ticket.getEntryFee()).multiply(BigDecimal.valueOf(ticket.getEntrants()));

            tvEventTicketPrice.setText(String.valueOf(totalFee));
            tvEventPersonsPaid.setText(String.valueOf(ticket.getEntrants()));

            Picasso.get().load("http://jakebreen.co.uk/android/shushevents/classimages/" + ticket.getCoverImage()).fit().into(ivEventImage);

            if (ticket.getRefunded() != 0) {
                tvEventTicketCancelled.setText("This class has been cancelled and your ticket has been refunded.");
            } else {
                tvEventTicketCancelled.setVisibility(View.GONE);
            }

            // Return the completed view to render on screen

            lvMyEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    lvMyEventList.setItemChecked(position, true);
                    lvMyEventList.setSelector(R.color.offWhite);

                    Ticket selectedTicket = (Ticket) lvMyEventList.getItemAtPosition(position);

                    Intent intent = new Intent(MyEventListActivity.this, TicketActivity.class);
                    intent.putExtra("ticket", (Serializable) selectedTicket);
                    startActivity(intent);

                }
            });

            return convertView;
        }
    }
}
