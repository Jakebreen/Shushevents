package uk.co.jakebreen.shushevents.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerResetPasswordViewComponent;
import uk.co.jakebreen.shushevents.injection.ResetPasswordViewModule;
import uk.co.jakebreen.shushevents.presenter.ResetPasswordPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.ResetPasswordView;

public final class ResetPassword extends BaseActivity<ResetPasswordPresenter, ResetPasswordView> implements ResetPasswordView {
    @Inject
    PresenterFactory<ResetPasswordPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.et_resetEmail)
    EditText etResetEamil;
    @BindView(R.id.btn_resetPassword)
    Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerResetPasswordViewComponent.builder()
                .appComponent(parentComponent)
                .resetPasswordViewModule(new ResetPasswordViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<ResetPasswordPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_resetPassword)
    public void onClickResetPassword() {
        String email = etResetEamil.getText().toString();

        if (mPresenter.validateForm(email)) {
            mPresenter.resetPassword(email);
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
