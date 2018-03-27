package uk.co.jakebreen.shushevents.data.remote;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.view.impl.LoginActivity;
import uk.co.jakebreen.shushevents.view.impl.RegisterAccountActivity;

import static uk.co.jakebreen.shushevents.data.remote.ApiUtils.BASE_URL;
import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAuth;

/**
 * Created by Jake on 15/03/2018.
 */

public class BackgroundService extends IntentService {

    private String TAG = BackgroundService.class.getSimpleName();
    private APIService mAPIService;
    private static Retrofit retrofit = null;
    private List<Account> accountArrayList;
    private String switchValue = null;
    private Bundle bundle;

    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switchValue = intent.getStringExtra("switchValue");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }

        mAPIService = ApiUtils.getAPIService();
        String userid = mAuth.getUid();

        switch (switchValue) {

            case "account":
                Call<List<Account>> call = mAPIService.getAccountByUserID(userid);
                try {
                    Response<List<Account>> response = call.execute();
                    accountArrayList = response.body();
                    Log.i(TAG, "User account requested from API: " + response.body().toString());
                } catch (IOException e) {
                    Log.e(TAG, "Unable to submit account to API: " + e);
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    Log.e(TAG, "Unable to receive account from API: " + e);
                    e.printStackTrace();
                }

                bundle = new Bundle();
                bundle.putParcelableArrayList("currentAccount", (ArrayList<? extends Parcelable>) accountArrayList);
                break;

            case "ticket":

                int idevent = Integer.parseInt(intent.getStringExtra("idevent"));

                Call<String> call2 = mAPIService.getRemainingTickets(idevent);
                try {
                    Response<String> response = call2.execute();
                    //accountArrayList = response.body();
                    response.body().indexOf(0);


                    Log.i(TAG, "Tickets remaining requested from API: " + response.body().toString());
                } catch (IOException e) {
                    Log.e(TAG, "Unable to submit tickets remaining to API: " + e);
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    Log.e(TAG, "Unable to receive tickets remaining from API: " + e);
                    e.printStackTrace();
                }

                bundle = new Bundle();
                //bundle.putInt("maxTickets", );
                //bundle.putParcelableArrayList("currentAccount", (ArrayList<? extends Parcelable>) accountArrayList);
                break;
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LoginActivity.ResponseReceiver.ACTION_RESP);
        broadcastIntent.setAction(RegisterAccountActivity.ResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.putExtra("currentAccount", (Serializable) accountArrayList);
        broadcastIntent.putExtras(bundle);
        sendBroadcast(broadcastIntent);

    }
}
