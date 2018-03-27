package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.interactor.EventInteractor;
import uk.co.jakebreen.shushevents.presenter.EventPresenter;
import uk.co.jakebreen.shushevents.view.EventView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class EventPresenterImpl extends BasePresenterImpl<EventView> implements EventPresenter {

    private String TAG = EventPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final EventInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public EventPresenterImpl(@NonNull EventInteractor interactor) {
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
    public void getInstructor(String userid) {
        mAPIService.getAccountByUserID(userid).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "account submitted to API." + response.body().toString());

                    List<Account> instructorList = response.body();
                    Account account = instructorList.get(0);

                    if (mView != null) mView.populateInstructorData(account);
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit account to API.");
            }
        });
    }

    @Override
    public void getVenue(int venueid) {
        Log.e(TAG, "venueidX " + venueid);
        mAPIService.getVenueById(venueid).enqueue(new Callback<List<Venue>>() {
            @Override
            public void onResponse(Call<List<Venue>> call, Response<List<Venue>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "venue submitted to API." + response.body().toString());

                    List<Venue> venueList = response.body();
                    Venue venue = venueList.get(0);

                    if (mView != null) mView.populateVenueData(venue);
                }
            }

            @Override
            public void onFailure(Call<List<Venue>> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit venue to API." + t);
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }
}