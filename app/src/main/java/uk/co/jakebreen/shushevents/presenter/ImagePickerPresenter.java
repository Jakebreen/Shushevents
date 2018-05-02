package uk.co.jakebreen.shushevents.presenter;

import uk.co.jakebreen.shushevents.view.ImagePickerView;

public interface ImagePickerPresenter extends BasePresenter<ImagePickerView> {

    void getCoverImages();

}