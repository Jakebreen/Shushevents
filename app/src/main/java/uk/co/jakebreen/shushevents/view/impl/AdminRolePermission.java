package uk.co.jakebreen.shushevents.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Role;
import uk.co.jakebreen.shushevents.injection.AdminRolePermissionViewModule;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerAdminRolePermissionViewComponent;
import uk.co.jakebreen.shushevents.presenter.AdminRolePermissionPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.AdminRolePermissionView;

public final class AdminRolePermission extends BaseActivity<AdminRolePermissionPresenter, AdminRolePermissionView> implements AdminRolePermissionView {
    @Inject
    PresenterFactory<AdminRolePermissionPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.et_adminSearchUser)
    EditText etAdminSearchUser;
    @BindView(R.id.btn_searchUserByEmail)
    ImageView btnSearchUserByEmail;
    @BindView(R.id.lv_searchUserList)
    ListView lvSeachUserList;
    @BindView(R.id.lv_selectRoleList)
    ListView lvSelectRoleList;
    @BindView(R.id.btn_adminRolePermissionUpdate)
    Button btnAdminRoleUpdate;

    private Role selectedRole;
    private Account selectedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_role_permission);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAdminRolePermissionViewComponent.builder()
                .appComponent(parentComponent)
                .adminRolePermissionViewModule(new AdminRolePermissionViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<AdminRolePermissionPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.btn_searchUserByEmail)
    public void onClickSearchUserByEmail() {
        searchUserByEmail();
    }

    @Override
    public void searchUserByEmail() {
        String email = etAdminSearchUser.getText().toString();

        if (email.equals(null) || email.isEmpty() || email.equals("")) {
            showToast("Email field is empty");
        } else {
            mPresenter.getUserAccountByEmail(email);
        }
    }

    @Override
    public void displayUsersList(List accountList) {
        // Construct the data source
        ArrayList<Account> arrayListAccount = new ArrayList<Account>(accountList);
        // Create the adapter to convert the array to views
        AccountAdapter adapter = new AccountAdapter(this, arrayListAccount);
        // Attach the adapter to a ListView
        lvSeachUserList.setAdapter(adapter);
    }

    public class AccountAdapter extends ArrayAdapter<Account> {
        public AccountAdapter(Context context, ArrayList<Account> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Account account = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_account_admin_search, parent, false);
            }
            // Lookup view for data population
            TextView tvAccountFirstname = (TextView) convertView.findViewById(R.id.tv_accountFirstname);
            TextView tvAccountSurname = (TextView) convertView.findViewById(R.id.tv_accountSurname);
            TextView tvAccountEmail = (TextView) convertView.findViewById(R.id.tv_accountEmail);
            TextView tvAccountUserID = (TextView) convertView.findViewById(R.id.tv_accountUserID);
            // Populate the data into the template view using the data object
            tvAccountFirstname.setText(account.getFirstname());
            tvAccountSurname.setText(account.getSurname());
            tvAccountEmail.setText(account.getEmail());
            tvAccountUserID.setText(account.getUserid());
            // Return the completed view to render on screen

            lvSeachUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    lvSeachUserList.setItemChecked(position, true);
                    lvSeachUserList.setSelector(R.color.offWhite);

                    selectedAccount = (Account) lvSeachUserList.getItemAtPosition(position);

                    mPresenter.getRoles();
                }
            });

            return convertView;
        }
    }

    @Override
    public void displayRolesList(List roleList) {
        ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(this, android.R.layout.simple_list_item_1, android.R.id.text1, roleList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Role role = getItem(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setText(role.getTitle());

                lvSelectRoleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long arg3) {
                        lvSelectRoleList.setItemChecked(position, true);
                        lvSelectRoleList.setSelector(R.color.offWhite);

                        selectedRole = (Role) lvSelectRoleList.getItemAtPosition(position);
                    }
                });
                return view;
            }
        };
        lvSelectRoleList.setAdapter(adapter);
    }

    @OnClick(R.id.btn_adminRolePermissionUpdate)
    public void onClickUpdateUserRole() {
        if (selectedAccount == null) {
            showToast("No user account selected.");
        } else if (selectedRole == null) {
            showToast("No role selected.");
        } else {
            mPresenter.sendAccountUpdateRole(selectedAccount.getUserid(), selectedRole.getIdRole(),
                    selectedAccount.getFirstname(), selectedAccount.getSurname(), selectedRole.getTitle());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
