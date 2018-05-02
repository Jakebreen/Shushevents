package uk.co.jakebreen.shushevents.view;

import android.support.annotation.UiThread;

import java.util.ArrayList;

@UiThread
public interface ImagePickerView {

    void showToast(String message);
    void cropImageTask();
    void displayCoverImageList(ArrayList<String> coverImageList);

}