package uk.co.jakebreen.shushevents.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.remote.BackgroundService;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerRegisterAccountViewComponent;
import uk.co.jakebreen.shushevents.injection.RegisterAccountViewModule;
import uk.co.jakebreen.shushevents.presenter.RegisterAccountPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.RegisterAccountView;

public final class RegisterAccountActivity extends BaseActivity<RegisterAccountPresenter, RegisterAccountView> implements RegisterAccountView {

    private String TAG = RegisterAccountActivity.class.getSimpleName();

    @Inject
    PresenterFactory<RegisterAccountPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.et_registerFirstname)
    EditText etFirstname;
    @BindView(R.id.et_registerSurname)
    EditText etSurname;
    @BindView(R.id.et_registerEmail)
    EditText etEmail;
    @BindView(R.id.et_registerPasswordFirst)
    EditText etPasswordFirst;
    @BindView(R.id.et_registerPasswordSecond)
    EditText etPasswordSecond;
    @BindView(R.id.btn_registerAccount)
    Button btnRegister;

    private ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerRegisterAccountViewComponent.builder()
                .appComponent(parentComponent)
                .registerAccountViewModule(new RegisterAccountViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<RegisterAccountPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(LoginActivity.ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
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

    @OnClick(R.id.btn_registerAccount)
    public void onClickValidateForm() {

        String f = String.valueOf(etFirstname.getText());
        f = f.trim();
        String s = String.valueOf(etSurname.getText());
        s = s.trim();
        String firstname = f.replaceAll("[^A-Za-z0-9]", "");
        String surname = s.replaceAll("[^A-Za-z0-9]", "");

        String email = String.valueOf(etEmail.getText());
        email = email.trim();

        String passwordFirst = String.valueOf(etPasswordFirst.getText());
        String passwordSecond = String.valueOf(etPasswordSecond.getText());

        if (mPresenter.validateForm(email, firstname, surname, passwordFirst, passwordSecond)) {
            mPresenter.createAccount(email, passwordFirst, firstname, surname);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    public void getCurrentAccount() {
        Intent intent = new Intent(this, BackgroundService.class);
        intent.putExtra("switchValue", "account");
        startService(intent);

        Log.d("responseReceiverX", "getCurrentAccount RegisterActivity");
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "uk.co.jakebreen.shushevents.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            ArrayList<Parcelable> currentAccount = bundle.getParcelableArrayList("currentAccount");
            Account account = (Account) currentAccount.get(0);

            Log.d("responseReceiverX", account.getUserid());

            setDefaults("userUserID", account.getUserid());
            setDefaults("userFirstname", account.getFirstname());
            setDefaults("userSurname", account.getSurname());
            setDefaults("userEmail", account.getEmail());
            setDefaults("userRoleID", account.getRoleid());

            Log.i(TAG, "User account: " + account.getEmail());

            unregisterReceiver(receiver);
            finishActivity();
        }
    }

    public void finishActivity() {
        finish();
    }
}
