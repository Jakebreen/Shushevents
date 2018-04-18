package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerFindEventsViewComponent;
import uk.co.jakebreen.shushevents.injection.FindEventsViewModule;
import uk.co.jakebreen.shushevents.presenter.FindEventsPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.FindEventsView;

public final class FindEventsActivity extends BaseActivity<FindEventsPresenter, FindEventsView> implements FindEventsView {
    @Inject
    PresenterFactory<FindEventsPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.et_findClass)
    EditText etFindClass;
    @BindView(R.id.tv_discover)
    TextView tvDiscover;
    @BindView(R.id.btn_findClasses)
    Button btnFindClasses;

    private Pair latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_events);
        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        //getSupportActionBar().hide();

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerFindEventsViewComponent.builder()
                .appComponent(parentComponent)
                .findEventsViewModule(new FindEventsViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<FindEventsPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get lat lng from prefs or move to next activity
        Double myLat = getDefaultsDouble("myLat");
        Double myLng = getDefaultsDouble("myLng");

        if (!myLat.equals(0.0) || !myLng.equals(0.0) || myLat != null || myLng != null) {
            Intent intent = new Intent(this, EventslistActivity.class);
            startActivity(intent);
        }

        // toolbar back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    @OnClick(R.id.btn_findClasses)
    public void onClickFindClasses() {
        String townPostcode = etFindClass.getText().toString();

        if (mPresenter.checkTownPostcode(townPostcode)) {
            latLng = mPresenter.getLatLng(townPostcode);
            if (!latLng.first.equals(0.0) && !latLng.second.equals(0.0) && !latLng.first.equals(null) && !latLng.second.equals(null)) {
                setDefaults("myLat", (Double) latLng.first);
                setDefaults("myLng", (Double) latLng.second);

                Intent intent = new Intent(this, EventslistActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
