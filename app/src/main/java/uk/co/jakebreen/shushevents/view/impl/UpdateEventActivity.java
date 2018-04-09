package uk.co.jakebreen.shushevents.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerUpdateEventViewComponent;
import uk.co.jakebreen.shushevents.injection.UpdateEventViewModule;
import uk.co.jakebreen.shushevents.presenter.UpdateEventPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.UpdateEventView;

public final class UpdateEventActivity extends BaseActivity<UpdateEventPresenter, UpdateEventView> implements UpdateEventView {

    private String TAG = UpdateEventActivity.class.getSimpleName();

    @BindView(R.id.ll_adminCancel)
    LinearLayout llAdminCancelEvent;

    @Inject
    PresenterFactory<UpdateEventPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerUpdateEventViewComponent.builder()
                .appComponent(parentComponent)
                .updateEventViewModule(new UpdateEventViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<UpdateEventPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @OnClick(R.id.ll_adminCancel)
    public void onClickCancelEvent() {
        Intent intent = new Intent(this, CancelEventActivity.class);
        startActivity(intent);
    }
}
