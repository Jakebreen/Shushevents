package uk.co.jakebreen.shushevents.view.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import uk.co.jakebreen.shushevents.App;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.remote.APIService;
import uk.co.jakebreen.shushevents.data.remote.ApiUtils;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.presenter.BasePresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterLoader;

public abstract class BaseActivity<P extends BasePresenter<V>, V> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<P> {

    private String TAG = BaseActivity.class.getSimpleName();

    //xlarge screens are at least 960dp x 720dp
    //large screens are at least 640dp x 480dp
    //normal screens are at least 470dp x 320dp
    //small screens are at least 426dp x 320dp

    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenter)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean mNeedToCallStart = new AtomicBoolean(false);
    /**
     * The presenter for this view
     */
    @Nullable
    protected P mPresenter;
    /**
     * Is this the first start of the activity (after onCreate)
     */
    private boolean mFirstStart;

    //Declare an instance of FirebaseAuth
    public static FirebaseAuth mAuth;
    public static APIService mAPIService;

    public static final String MyPREFERENCES = "MyPrefs";
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFirstStart = true;

        injectDependencies();

        getSupportLoaderManager().initLoader(0, null, this).startLoading();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        mAPIService = ApiUtils.getAPIService();
    }

    private void injectDependencies() {
        setupComponent(((App) getApplication()).getAppComponent());
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mPresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    /**
     * Call the presenter callbacks for onStart
     */
    @SuppressWarnings("unchecked")
    private void doStart() {
        assert mPresenter != null;

        mPresenter.onViewAttached((V) this);

        mPresenter.onStart(mFirstStart);

        mFirstStart = false;

        ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();

            mPresenter.onViewDetached();
        }

        super.onStop();

        mProgressDialog.dismiss();
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, getPresenterFactory());
    }

    @Override
    public final void onLoadFinished(Loader<P> loader, P presenter) {
        mPresenter = presenter;

        if (mNeedToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }

    /**
     * Get the presenter factory implementation for this view
     *
     * @return the presenter factory
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Setup the injection component for this view
     *
     * @param appComponent the app component
     */
    protected abstract void setupComponent(@NonNull AppComponent appComponent);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.item_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.item_settings:
                intent = new Intent(this, AccountMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.item_admin:
                intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                break;
            case R.id.item_logout:
                removeDefault("userUserID");
                removeDefault("userFirstname");
                removeDefault("userSurname");
                removeDefault("userEmail");
                removeDefault("userRoleID");
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signed out of account", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 0 login
        // 1 account settings
        // 2 admin settings
        // 3 logout
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            menu.getItem(0).setVisible(true);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(3).setVisible(true);
        }

        //roleID 1 is a No Permission user
        if (getDefaultsInt("userRoleID") > 1) {
            menu.getItem(2).setVisible(true);
        } else {
            menu.getItem(2).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public  void setDefaults(String key, String value) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public  String getDefaultsString(String key) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public void setDefaults(String key, Double value) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, Double.doubleToRawLongBits((Double) value));
        editor.commit();
    }

    public Double getDefaultsDouble(String key) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return Double.longBitsToDouble(preferences.getLong(key, Double.doubleToLongBits(0.0)));
    }

    public void setDefaults(String key, int value) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getDefaultsInt(String key) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public void setDefaults(String key, BigDecimal value) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    public void removeDefault(String key) {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        preferences.edit().remove(key).commit();
    }

    public void showDialog(String message) {
        mProgressDialog.show();
        mProgressDialog.setMessage(message);
    }

    public void hideDialog() {
        mProgressDialog.hide();
    }
}
