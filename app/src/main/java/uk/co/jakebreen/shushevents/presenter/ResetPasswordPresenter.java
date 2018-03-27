package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.ResetPasswordView;

public interface ResetPasswordPresenter extends BasePresenter<ResetPasswordView> {

    boolean validateForm(String email);
    boolean isEmailValid(String email);
    void resetPassword(String email);

}