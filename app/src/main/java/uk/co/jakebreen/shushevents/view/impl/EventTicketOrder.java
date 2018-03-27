package uk.co.jakebreen.shushevents.view.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.data.remote.BackgroundService;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerEventTicketOrderViewComponent;
import uk.co.jakebreen.shushevents.injection.EventTicketOrderViewModule;
import uk.co.jakebreen.shushevents.presenter.EventTicketOrderPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.EventTicketOrderView;

public final class EventTicketOrder extends BaseActivity<EventTicketOrderPresenter, EventTicketOrderView> implements EventTicketOrderView {
    //Braintree
    private static final String TAG = EventTicketOrder.class.getSimpleName();

    // Your presenter is available using the mPresenter variable
    private static final String PATH_TO_SERVER = "http://www.jakebreen.co.uk/android/shushevents/braintreepayments/index.php";
    private static final int BRAINTREE_REQUEST_CODE = 4949;
    protected FirebaseUser user;
    @Inject
    PresenterFactory<EventTicketOrderPresenter> mPresenterFactory;
    @BindView(R.id.tv_eventTicketOrderTitle)
    TextView tvEventTicketOrderTitle;
    @BindView(R.id.tv_eventTicketOrderAddress)
    TextView tvEventTicketOrderAddress;
    @BindView(R.id.tv_eventTicketOrderDateTime)
    TextView tvEventTicketOrderDateTime;
    @BindView(R.id.tv_eventTicketOrderDuration)
    TextView tvEventTicketOrderDuration;
    @BindView(R.id.tv_eventTicketOrderTicketFee)
    TextView tvEventTicketOrderTicketFee;
    //@BindView(R.id.spn_ticketOrderNumberPurchased)
    //Spinner spnTicketOrderNumberPurchased;
    @BindView(R.id.tv_ticketOrderTicketCalculation)
    TextView tvTicketOrderTicketCalculation;
    @BindView(R.id.tv_ticketOrderTicketTotal)
    TextView tvTicketOrderTicketTotal;
    @BindView(R.id.btn_ticketOrderPurchase)
    Button btnTicketOrderPurchase;

    private boolean paymentInProgress;
    private String clientToken;
    private Event mEvent;
    private Venue mVenue;
    private BigDecimal ticketFee, ticketTotal;
    private List<String> ticketOrderNum;
    private int selectedTicketTotal;
    private Spinner spnTicketOrderNumberPurchased;

    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_ticket_order);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getClientTokenFromServer();

        Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra("event");
        mVenue = (Venue) intent.getSerializableExtra("venue");

        //check available tickets
        checkAvailability();

        ticketFee = new BigDecimal(mEvent.getEntryFee());

        spnTicketOrderNumberPurchased = (Spinner) findViewById(R.id.spn_ticketOrderNumberPurchased);

        spnTicketOrderNumberPurchased.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTicketTotal = Integer.parseInt(spnTicketOrderNumberPurchased.getSelectedItem().toString());
                //iSelectedTicketTotal = (Integer)spnTicketOrderNumberPurchased.getSelectedItem();
                displayTotal(selectedTicketTotal, ticketFee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        ArrayAdapter ticketAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, ticketOrderNum);
        spnTicketOrderNumberPurchased.setAdapter(ticketAdapter);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerEventTicketOrderViewComponent.builder()
                .appComponent(parentComponent)
                .eventTicketOrderViewModule(new EventTicketOrderViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(receiver);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @NonNull
    @Override
    protected PresenterFactory<EventTicketOrderPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //disable button until braintree ready
        btnTicketOrderPurchase.setEnabled(false);

        IntentFilter filter = new IntentFilter(RegisterAccountActivity.ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new EventTicketOrder.ResponseReceiver();
        registerReceiver(receiver, filter);

        getClientTokenFromServer();

        tvEventTicketOrderTitle.setText(mEvent.getTitle());
        tvEventTicketOrderAddress.setText(mVenue.getHandle() + " - " + mVenue.getTown() + ", " + mVenue.getAddress() + " " + mVenue.getPostcode());
        tvEventTicketOrderDateTime.setText(mEvent.getDate() + " - " + mEvent.getTime());
        tvEventTicketOrderDuration.setText(mEvent.getDuration());
        tvEventTicketOrderTicketFee.setText("£" + mEvent.getEntryFee() + " per person");
    }

    @Override
    public void onBackPressed() {
        if (paymentInProgress == true) {
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public int checkAvailability() {
        int remainingTickets = mEvent.getMaxTickets() - mEvent.getEntrants();

        if (remainingTickets <= 0) {
            showToast("This class is fully booked");
            finish();
        } else if (remainingTickets == 1) {
            ticketOrderNum = new ArrayList<String>();
            ticketOrderNum.add("1");
        } else if (remainingTickets == 2) {
            ticketOrderNum = new ArrayList<String>();
            ticketOrderNum.add("1");
            ticketOrderNum.add("2");
        } else if (remainingTickets == 3) {
            ticketOrderNum = new ArrayList<String>();
            ticketOrderNum.add("1");
            ticketOrderNum.add("2");
            ticketOrderNum.add("3");
        } else if (remainingTickets == 4) {
            ticketOrderNum = new ArrayList<String>();
            ticketOrderNum.add("1");
            ticketOrderNum.add("2");
            ticketOrderNum.add("3");
            ticketOrderNum.add("4");
        } else if (remainingTickets >= 5) {
            ticketOrderNum = new ArrayList<String>();
            ticketOrderNum.add("1");
            ticketOrderNum.add("2");
            ticketOrderNum.add("3");
            ticketOrderNum.add("4");
            ticketOrderNum.add("5");
        }

        return remainingTickets;
    }

    @OnClick(R.id.btn_ticketOrderPurchase)
    public void onClickPurchaseTickets() {
        //checkTicketsAvailable();

        DropInRequest dropInRequest = new DropInRequest().clientToken(clientToken);
        startActivityForResult(dropInRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
    }

    @Override
    public void displayTotal(int selectedTicketTotal, BigDecimal entryFee) {
        tvTicketOrderTicketCalculation.setText("£" + entryFee + " x " + selectedTicketTotal + " tickets");
        ticketTotal = mPresenter.calculateTicketTotal(selectedTicketTotal, entryFee);
        tvTicketOrderTicketTotal.setText("£" + ticketTotal);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
                btnTicketOrderPurchase.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (RESULT_OK == resultCode) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentNonce = result.getPaymentMethodNonce().getNonce();

                sendPaymentNonceToServer(paymentNonce, String.valueOf(ticketTotal), getDefaultsString("userFirstname"), getDefaultsString("userSurname"), getDefaultsString("userEmail"), selectedTicketTotal);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "User cancelled payment");
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d(TAG, "error exception: " + error);
            }
        }
    }

    private void checkTicketsAvailable() {
        Intent intent = new Intent(this, BackgroundService.class);
        intent.putExtra("switchValue", "ticket");
        intent.putExtra("idevent", mEvent.getIdevent());
        startService(intent);
    }

    private void sendPaymentNonceToServer(String paymentNonce, String amount, String firstname, String surname, String email, final int tickets) {

        final int idevent = mEvent.getIdevent();

        //check remaining tickets before buying
        //int remainingTickets = mEvent.getMaxTickets() - mEvent.getEntrants();
        //if (tickets > remainingTickets) {
        //    showToast("Not enough tickets remaining for this class, please try again");
        //    finish();
        //    return;
        //}

        mProgressDialog.setCancelable(false);
        showDialog("Verifying purchase");
        paymentInProgress = true;

        RequestParams params = new RequestParams();
        params.add("NONCE", paymentNonce);
        params.add("AMOUNT", amount);
        params.add("FIRSTNAME", firstname);
        params.add("SURNAME", surname);
        params.add("EMAIL", email);

        AsyncHttpClient androidClient = new AsyncHttpClient();
        androidClient.post(PATH_TO_SERVER, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast("Failed: " + responseString);
                Log.d(TAG, "Error: Failed to create a transaction" + responseString);
                hideDialog();
                paymentInProgress = false;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();

                Log.d(TAG, "Output " + responseString);

                String[] splited = responseString.split("\\s+");
                String txid = splited[1];
                Log.d(TAG, "OutputID " + txid + " " + idevent);

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());

                mPresenter.sendTicket(userid, idevent, txid, date, selectedTicketTotal);
                Log.d(TAG, "TicketOutput " + selectedTicketTotal);

                showToast("Payment method accepted");
                hideDialog();

                paymentInProgress = false;
            }
        });
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "uk.co.jakebreen.shushevents.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            ArrayList<Parcelable> currentAccount = bundle.getParcelableArrayList("currentAccount");
            //bundle.getInt()

            //Catch non-synced accounts?
            if (currentAccount == null) {
                hideDialog();
                showToast("Account does not exist");
                return;
            }

            Account account = (Account) currentAccount.get(0);

            unregisterReceiver(receiver);
            hideDialog();
        }
    }
}
