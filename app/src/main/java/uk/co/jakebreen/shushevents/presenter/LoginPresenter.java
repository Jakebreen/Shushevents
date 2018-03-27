package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.LoginView;

public interface LoginPresenter extends BasePresenter<LoginView> {

    boolean login(String email, String password);

}