package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

@UiThread
public interface LoginView {

    void showToast(String message);
    void getCurrentAccount();
    void finishActivity();

}