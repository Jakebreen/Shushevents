package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.RegisterAccountView;

public interface RegisterAccountPresenter extends BasePresenter<RegisterAccountView> {

    void createAccount(String email, String password, String firstname, String surname);
    boolean validateForm(String email, String firstname, String surname, String passwordFirst, String passwordSecond);
    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    void sendAccount(String userid, String firstname, String surname, String email);
}