package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

@UiThread
public interface ResetPasswordView {

    void showToast(String message);
    void finishActivity();

}