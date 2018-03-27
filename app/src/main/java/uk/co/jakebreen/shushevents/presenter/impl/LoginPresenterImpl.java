package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import uk.co.jakebreen.shushevents.interactor.LoginInteractor;
import uk.co.jakebreen.shushevents.presenter.LoginPresenter;
import uk.co.jakebreen.shushevents.view.LoginView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAuth;

public final class LoginPresenterImpl extends BasePresenterImpl<LoginView> implements LoginPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final LoginInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public LoginPresenterImpl(@NonNull LoginInteractor interactor) {
        mInteractor = interactor;
    }

    private String TAG = LoginPresenterImpl.class.getSimpleName();
    private int counter;

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
    public boolean login(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //counter = 1;
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                user = mAuth.getCurrentUser();
                                mView.showToast("Logged in to account " + user.getEmail());
                                mView.getCurrentAccount();
                            } else {
                                //counter = 0;
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                mView.showToast("ERR: " + task.getException().getMessage());
                            }
                            // ...
                        }
                    });
            //if (counter == 1) {
            //    return true;
            //} else {
                return false;
            //}
        } else {
            mView.showToast("Please enter email and password");
            return false;
        }
    }
}