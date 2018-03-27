package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.remote.APIService;
import uk.co.jakebreen.shushevents.data.remote.ApiUtils;
import uk.co.jakebreen.shushevents.interactor.RegisterAccountInteractor;
import uk.co.jakebreen.shushevents.presenter.RegisterAccountPresenter;
import uk.co.jakebreen.shushevents.view.RegisterAccountView;
import uk.co.jakebreen.shushevents.view.impl.RegisterAccountActivity;

public final class RegisterAccountPresenterImpl extends BasePresenterImpl<RegisterAccountView> implements RegisterAccountPresenter {

    private String TAG = RegisterAccountActivity.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final RegisterAccountInteractor mInteractor;

    // The view is available using the mView variable

    private FirebaseAuth mAuth;
    protected FirebaseUser user;
    private APIService mAPIService;

    @Inject
    public RegisterAccountPresenterImpl(@NonNull RegisterAccountInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
        mAuth = FirebaseAuth.getInstance();
        mAPIService = ApiUtils.getAPIService();
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
    public void createAccount(String email, String password, final String firstname, final String surname) {
        Log.d(TAG, "signIn:" + email);
        mView.showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            String email = user.getEmail();

                            sendAccount(uid, firstname, surname, email);

                            mView.showToast("Your account has been created");
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            mView.showToast("Error: " + task.getException().getMessage());
                        }
                    }
                });

        mView.hideProgressDialog();
    }

    @Override
    public boolean validateForm(String email, String firstname, String surname, String passwordFirst, String passwordSecond) {

        if (!isEmailValid(email)) {
            mView.showToast("Email invalid");
            return false;
        }

        if (!isPasswordValid(passwordFirst)) {
            mView.showToast("Password should be at least 6 characters");
            return false;
        }

        if (!passwordFirst.equals(passwordSecond)) {
            mView.showToast("Passwords do not match");
            return false;
        }

        if (firstname.length() == 0) {
            mView.showToast("Please provide your firstname");
        }

        if (surname.length() == 0) {
            mView.showToast("Please provide your surname");
        }

        return true;
    }

    @Override
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public boolean isPasswordValid(String password) {
        if (!(password.length() > 0)) {
            return false;
        }
        return true;
    }

    @Override
    public void sendAccount(String userid, String firstname, String surname, String email) {

        //Set no permission user
        int roleid = 1;
        Log.d(TAG, userid + " " + firstname + " " + surname + " " + email + " " + roleid );
        mAPIService.saveAccount(userid, firstname, surname, email, roleid).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "account submitted to API." + response.body().toString());
                    mView.getCurrentAccount();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit account to API.");
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }
}