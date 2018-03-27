package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.List;

import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Role;

@UiThread
public interface AdminRolePermissionView {

    void showToast(String message);
    void searchUserByEmail();
    void displayUsersList(List<Account> accountList);
    void displayRolesList(List<Role> roleList);
    void finishActivity();

}