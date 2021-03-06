package uk.co.jakebreen.shushevents.presenter.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.interactor.EventslistInteractor;
import uk.co.jakebreen.shushevents.presenter.EventslistPresenter;
import uk.co.jakebreen.shushevents.view.EventslistView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class EventslistPresenterImpl extends BasePresenterImpl<EventslistView> implements EventslistPresenter {

    private String TAG = EventslistPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final EventslistInteractor mInteractor;

    // The view is available using the mView variable

    @BindView(R.id.lv_eventList)
    ListView lvEventList;

    private Account account;
    private Venue venue;

    @Inject
    public EventslistPresenterImpl(@NonNull EventslistInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }

    @Override
    public String getTown(Double lat, Double lng) {
        try {
            Geocoder geocoder = new Geocoder((Context) mView);
            List<Address> town = geocoder.getFromLocation(lat, lng, 1);

            Log.i(TAG, String.valueOf(town));

            //if (town.get(0).getAddressLine(1).equals("UK")) {
            //    return town.get(0).getAddressLine(0);
            //}

            //return town.get(0).getAddressLine(1);
            return town.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void getEvents(Double lat, Double lng) {
        mAPIService.getEventByLatLng(lat, lng).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Event> eventList = response.body();
                    if (mView != null) mView.displayEventList(eventList);

                    Log.i(TAG, "Event request submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                mView.showToast("No classes near you yet...");
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit event request to API.");
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }

    @Override
    public Account getInstructor(String userid) {
        mAPIService.getAccountByUserID(userid).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "account submitted to API." + response.body().toString());

                    List<Account> instructorList = response.body();
                    account = instructorList.get(0);

                    //if (mView != null) mView.populateInstructorData(account);
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit account to API.");
            }
        });

        return account;
    }

    @Override
    public Venue getVenue(int venueid) {
        Log.e(TAG, "venueidX " + venueid);
        mAPIService.getVenueById(venueid).enqueue(new Callback<List<Venue>>() {
            @Override
            public void onResponse(Call<List<Venue>> call, Response<List<Venue>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "venue submitted to API." + response.body().toString());

                    List<Venue> venueList = response.body();
                    venue = venueList.get(0);

                    //if (mView != null) mView.populateVenueData(venue);
                }
            }

            @Override
            public void onFailure(Call<List<Venue>> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit venue to API." + t);
            }
        });

        return venue;
    }
}