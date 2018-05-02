package uk.co.jakebreen.shushevents.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.jakebreen.shushevents.interactor.ImagePickerInteractor;
import uk.co.jakebreen.shushevents.presenter.ImagePickerPresenter;
import uk.co.jakebreen.shushevents.view.ImagePickerView;

import static uk.co.jakebreen.shushevents.view.impl.BaseActivity.mAPIService;

public final class ImagePickerPresenterImpl extends BasePresenterImpl<ImagePickerView> implements ImagePickerPresenter {

    private String TAG = ImagePickerPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final ImagePickerInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ImagePickerPresenterImpl(@NonNull ImagePickerInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }

    @Override
    public void getCoverImages() {
        mAPIService.getCoverImages().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    ArrayList<String> coverImageList = response.body();

                    if (mView == null) {
                        Log.i(TAG, "mView is null." + response.body().toString());
                    } else {
                        mView.displayCoverImageList(coverImageList);
                    }
                    Log.i(TAG, "Image request submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                mView.showToast("Unable to find imges");
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit image request to API.");
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }
}