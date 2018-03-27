package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.model.Ticket;
import uk.co.jakebreen.shushevents.interactor.EventTicketOrderInteractor;
import uk.co.jakebreen.shushevents.presenter.EventTicketOrderPresenter;
import uk.co.jakebreen.shushevents.view.EventTicketOrderView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class EventTicketOrderPresenterImpl extends BasePresenterImpl<EventTicketOrderView> implements EventTicketOrderPresenter {

    private String TAG = EventTicketOrderPresenterImpl.class.getSimpleName();
    private List<String> ticketOrderNum;

    /**
     * The interactor
     */
    @NonNull
    private final EventTicketOrderInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public EventTicketOrderPresenterImpl(@NonNull EventTicketOrderInteractor interactor) {
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
    public BigDecimal calculateTicketTotal(int selectedTicketTotal, BigDecimal entryFee) {
        BigDecimal itemCost  = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        itemCost  = entryFee.multiply(new BigDecimal(selectedTicketTotal));
        totalCost = totalCost.add(itemCost);
        return totalCost;
    }

    @Override
    public void sendTicket(String userid, int idevent, String txid, String txDateTime, int entrants) {
        mAPIService.saveTicket(userid, idevent, txid, txDateTime, entrants).enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "ticket submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit ticket to API.");
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }
}