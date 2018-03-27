package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.view.AdminRolePermissionView;

public interface AdminRolePermissionPresenter extends BasePresenter<AdminRolePermissionView> {

    Account getUserAccountByEmail(String email);
    void getRoles();
    void sendAccountUpdateRole(String userid, int roleid, String firstname, String surname, String roleTitle);

}