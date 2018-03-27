package uk.co.jakebreen.shushevents.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import javax.inject.Inject;

import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerSignupViewComponent;
import uk.co.jakebreen.shushevents.injection.SignupViewModule;
import uk.co.jakebreen.shushevents.presenter.SignupPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.SignupView;

public final class SignupActivity extends BaseActivity<SignupPresenter, SignupView> implements SignupView {
    @Inject
    PresenterFactory<SignupPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerSignupViewComponent.builder()
                .appComponent(parentComponent)
                .signupViewModule(new SignupViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<SignupPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
