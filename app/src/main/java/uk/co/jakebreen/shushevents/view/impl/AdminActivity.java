package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AdminViewModule;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerAdminViewComponent;
import uk.co.jakebreen.shushevents.presenter.AdminPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.AdminView;

public final class AdminActivity extends BaseActivity<AdminPresenter, AdminView> implements AdminView {

    private String TAG = AdminActivity.class.getSimpleName();

    @Inject
    PresenterFactory<AdminPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.ll_adminRoles)
    LinearLayout llAdminRoles;
    @BindView(R.id.ll_adminCreateEvent)
    LinearLayout llAdminEvent;
    @BindView(R.id.ll_adminEditEvent)
    LinearLayout llAdminEditEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAdminViewComponent.builder()
                .appComponent(parentComponent)
                .adminViewModule(new AdminViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int roleid = getDefaultsInt("userRoleID");

        llAdminRoles.setVisibility(View.GONE);
        llAdminEvent.setVisibility(View.GONE);
        llAdminEditEvent.setVisibility(View.GONE);

        if (roleid == 2) {
            llAdminRoles.setVisibility(View.VISIBLE);
            llAdminEvent.setVisibility(View.VISIBLE);
            llAdminEditEvent.setVisibility(View.VISIBLE);
        } else if (roleid == 3) {
            llAdminEvent.setVisibility(View.VISIBLE);
            llAdminEditEvent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @NonNull
    @Override
    protected PresenterFactory<AdminPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @OnClick(R.id.ll_adminRoles)
    public void onClickAdminRoles() {
        Intent intent = new Intent(this, AdminRolePermission.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_adminCreateEvent)
    public void onClickAdminEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_adminEditEvent)
    public void onClickAdminEditEvent() {
        Intent intent = new Intent(this, UpdateEventActivity.class);
        startActivity(intent);
    }

}
