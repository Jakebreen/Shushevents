package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.model.Ticket;
import uk.co.jakebreen.shushevents.interactor.MyEventListInteractor;
import uk.co.jakebreen.shushevents.presenter.MyEventListPresenter;
import uk.co.jakebreen.shushevents.view.MyEventListView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class MyEventListPresenterImpl extends BasePresenterImpl<MyEventListView> implements MyEventListPresenter {

    private String TAG = MyEventListPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final MyEventListInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public MyEventListPresenterImpl(@NonNull MyEventListInteractor interactor) {
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
    public void getEvents(String userid) {
        mAPIService.getPaidEventByUserID(userid).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Ticket> eventList = response.body();
                    mView.displayEventList(eventList);

                    Log.i(TAG, "Event request submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                mView.showToast("No upcoming classes");
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit event request to API.");
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }
}