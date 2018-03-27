package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

@UiThread
public interface RegisterAccountView {

    void showToast(String message);
    void showProgressDialog();
    void hideProgressDialog();
    void finishActivity();
    void getCurrentAccount();

}