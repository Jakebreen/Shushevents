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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.remote.BackgroundService;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerLoginViewComponent;
import uk.co.jakebreen.shushevents.injection.LoginViewModule;
import uk.co.jakebreen.shushevents.presenter.LoginPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.LoginView;

public final class LoginActivity extends BaseActivity<LoginPresenter, LoginView> implements LoginView {

    private String TAG = LoginActivity.class.getSimpleName();

    @Inject
    PresenterFactory<LoginPresenter> mPresenterFactory;

    @BindView(R.id.et_loginEmail)
    EditText etLoginEmail;
    @BindView(R.id.et_loginPassword)
    EditText etLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_loginRegister)
    TextView tvLoginRegister;
    @BindView(R.id.login_button)
    LoginButton btn_FacebookLogin;

    protected FirebaseUser user;
    private ResponseReceiver receiver;
    private CallbackManager mCallbackManager;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.


    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerLoginViewComponent.builder()
                .appComponent(parentComponent)
                .loginViewModule(new LoginViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<LoginPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        btn_FacebookLogin.setReadPermissions("email");
        btn_FacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i(TAG, "Hello" + loginResult.getAccessToken().getToken());
                //  Toast.makeText(MainActivity.this, "Token:"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //name = user.getDisplayName();
                    Toast.makeText(LoginActivity.this, "" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(receiver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_login)
    public void onClickogin() {
        //if (!mPresenter.checkLoggedOn()) {
        //    showToast("no login");
        //} else {
        //    user = FirebaseAuth.getInstance().getCurrentUser();
        //    showToast(user.getUid());
        //}
        String email = etLoginEmail.getText().toString();
        email = email.trim();
        String password = etLoginPassword.getText().toString();

        mPresenter.login(email, password);
    }

    @OnClick(R.id.tv_loginRegister)
    public void onClickRegisterActivity() {
        Intent intent = new Intent(this, RegisterAccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void getCurrentAccount() {
        showDialog("One moment.. verifying user details");
        Intent intent = new Intent(this, BackgroundService.class);
        intent.putExtra("switchValue", "account");
        startService(intent);
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "uk.co.jakebreen.shushevents.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            ArrayList<Parcelable> currentAccount = bundle.getParcelableArrayList("currentAccount");

            //Catch non-synced accounts?
            if (currentAccount == null) {
                hideDialog();
                showToast("Account does not exist");
                return;
            }

            Account account = (Account) currentAccount.get(0);

            Log.d("responseReceiverX", account.getUserid());

            setDefaults("userUserID", account.getUserid());
            setDefaults("userFirstname", account.getFirstname());
            setDefaults("userSurname", account.getSurname());
            setDefaults("userEmail", account.getEmail());
            setDefaults("userRoleID", account.getRoleid());

            Log.i(TAG, "User account: " + account.getEmail());

            unregisterReceiver(receiver);
            hideDialog();
            finishActivity();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}
