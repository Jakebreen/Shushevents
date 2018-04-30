package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.firebase.MyFirebaseMessagingService;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.interactor.CancelEventInteractor;
import uk.co.jakebreen.shushevents.presenter.CancelEventPresenter;
import uk.co.jakebreen.shushevents.view.CancelEventView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class CancelEventPresenterImpl extends BasePresenterImpl<CancelEventView> implements CancelEventPresenter {

    private String TAG = CancelEventPresenterImpl.class.getSimpleName();

    private ArrayList<String> tokenList;

    /**
     * The interactor
     */
    @NonNull
    private final CancelEventInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public CancelEventPresenterImpl(@NonNull CancelEventInteractor interactor) {
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
    public String onDateSetFormatDate(int startYear, int startMonth, int startDay) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.e(TAG, String.valueOf(currentTime));

        Calendar calander = Calendar.getInstance();
        calander.setTimeInMillis(0);
        calander.set(startYear, startMonth, startDay, 0, 0, 0);
        Date SelectedDate = calander.getTime();

        if (SelectedDate.before(currentTime)) {
            mView.showToast("Select a future date");
            return null;
        } else {
            DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String StringDateformat_UK = dateformat_UK.format(SelectedDate);
            return StringDateformat_UK;
        }
    }

    @Override
    public void getEvents(String date) {
        mAPIService.getEventByDate(date).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Event> eventList = response.body();
                    mView.displayEventList(eventList);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                mView.showToast("No classes for this date found");
                showResponse(t.toString());
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }

    @Override
    public void cancelEvent(final int eventid, final String title, final String message) {

        //Call to update event and refund all attendees
        mAPIService.cancelEvent(eventid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>  call, Response<ResponseBody>  response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    //Call to get firebase tokens of attendees.
                    mAPIService.getEventAttendees(eventid).enqueue(new Callback<ArrayList<String>>() {
                        @Override
                        public void onResponse(Call<ArrayList<String>>  call, Response<ArrayList<String>>  response) {
                            if(response.isSuccessful()) {
                                tokenList = response.body();
                                String tokenString = tokenList.toString();
                                //Send notification to all attendees
                                new MyFirebaseMessagingService().sendNotificationToIDs(tokenString, title, message);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<String>>  call, Throwable t) {
                            showResponse(t.toString());
                        }
                    });

                    if (mView != null) mView.showToast("Event cancelled and refunded");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>  call, Throwable t) {
                mView.showToast("Problem cancelling event");
                showResponse(t.toString());
            }
        });
    }
}