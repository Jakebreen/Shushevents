package uk.co.jakebreen.shushevents.data.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;
import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAuth;

/**
 * Created by Jake on 20/04/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String accountFbToken;

    private String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    public MyFirebaseInstanceIDService() {
        super();
    }

    public MyFirebaseInstanceIDService(String token) {
        this.accountFbToken = token;
        onTokenRefresh();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

        compareTokens(refreshedToken, accountFbToken);
    }

    private void compareTokens(String refreshedToken, String accountFbToken) {

        if (!refreshedToken.equals(accountFbToken)) {
            sendRegistrationToServer(refreshedToken);
            Log.d(TAG, "Token mismatch, refreshed token = " + refreshedToken + " account token = " + accountFbToken);
        } else {
            Log.d(TAG, "Tokens match, token = " + refreshedToken);
        }

    }

    private void sendRegistrationToServer(String token) {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mAPIService.updateAccountFirebaseToken(token, user.getUid()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        showResponse(response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody>  call, Throwable t) {
                    showResponse(t.toString());
                }
            });
        }
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }

}
