package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import uk.co.jakebreen.shushevents.interactor.ResetPasswordInteractor;
import uk.co.jakebreen.shushevents.presenter.ResetPasswordPresenter;
import uk.co.jakebreen.shushevents.view.ResetPasswordView;

public final class ResetPasswordPresenterImpl extends BasePresenterImpl<ResetPasswordView> implements ResetPasswordPresenter {

    private String TAG = ResetPasswordPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final ResetPasswordInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ResetPasswordPresenterImpl(@NonNull ResetPasswordInteractor interactor) {
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
    public boolean validateForm(String email) {

        if (!isEmailValid(email)) {
            mView.showToast("Email invalid");
            return false;
        }

        if (email.equals(null) || email.equals("")) {
            mView.showToast("Email field empty");
            return false;
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
    public void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mView.showToast("Password reset, check your email inbox");
                            Log.d(TAG, "Password reset email sent.");
                            mView.finishActivity();
                        } else {
                            mView.showToast("Could'nt find an account with that email address");
                        }
                    }
                });
    }
}