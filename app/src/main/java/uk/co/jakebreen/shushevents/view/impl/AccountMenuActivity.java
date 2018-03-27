package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AccountMenuViewModule;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerAccountMenuViewComponent;
import uk.co.jakebreen.shushevents.presenter.AccountMenuPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.AccountMenuView;

public final class AccountMenuActivity extends BaseActivity<AccountMenuPresenter, AccountMenuView> implements AccountMenuView {
    @Inject
    PresenterFactory<AccountMenuPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.ll_accountResetPassword)
    LinearLayout llAccountResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_menu);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAccountMenuViewComponent.builder()
                .appComponent(parentComponent)
                .accountMenuViewModule(new AccountMenuViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<AccountMenuPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @OnClick(R.id.ll_accountResetPassword)
    public void onClickPasswordReset() {
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }
}
