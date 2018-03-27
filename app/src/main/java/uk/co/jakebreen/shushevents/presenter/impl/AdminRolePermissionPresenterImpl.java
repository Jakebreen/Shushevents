package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Role;
import uk.co.jakebreen.shushevents.data.remote.APIService;
import uk.co.jakebreen.shushevents.data.remote.ApiUtils;
import uk.co.jakebreen.shushevents.interactor.AdminRolePermissionInteractor;
import uk.co.jakebreen.shushevents.presenter.AdminRolePermissionPresenter;
import uk.co.jakebreen.shushevents.view.AdminRolePermissionView;

public final class AdminRolePermissionPresenterImpl extends BasePresenterImpl<AdminRolePermissionView> implements AdminRolePermissionPresenter {

    private String TAG = AdminRolePermissionPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final AdminRolePermissionInteractor mInteractor;

    // The view is available using the mView variable

    private APIService mAPIService;

    @Inject
    public AdminRolePermissionPresenterImpl(@NonNull AdminRolePermissionInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
        mAPIService = ApiUtils.getAPIService();
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
    public Account getUserAccountByEmail(String email) {
        mAPIService.getAccountByEmail(email).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Account> listAccount = response.body();
                    mView.displayUsersList(listAccount);

                    Log.i(TAG, "Email submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                mView.showToast("Unable to find user account");
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit user email request to API.");
            }
        });

        return null;
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }

    @Override
    public void getRoles() {
        mAPIService.getRole().enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(Call<List<Role>> call, Response<List<Role>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Role> roleList = response.body();
                    mView.displayRolesList(roleList);

                    Log.i(TAG, "Role request submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Role>> call, Throwable t) {
                mView.showToast("Unable to find roles");
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit role request to API.");
            }
        });
    }

    @Override
    public void sendAccountUpdateRole(String userid, int roleid, final String firstname, final String surname, final String roleTitle) {
        mAPIService.updateAccountRole(userid, roleid).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "update submitted to API." + response.body().toString());
                    mView.showToast("User: " + firstname + " " + surname + " now has " + roleTitle + " permissions.");
                    mView.finishActivity();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit update to API.");
            }
        });

    }
}