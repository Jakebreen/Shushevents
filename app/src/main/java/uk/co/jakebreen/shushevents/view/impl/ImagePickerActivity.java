package uk.co.jakebreen.shushevents.view.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.OnClick;
import uk.co.jakebreen.shushevents.R;
import uk.co.jakebreen.shushevents.injection.AppComponent;
import uk.co.jakebreen.shushevents.injection.DaggerImagePickerViewComponent;
import uk.co.jakebreen.shushevents.injection.ImagePickerViewModule;
import uk.co.jakebreen.shushevents.presenter.ImagePickerPresenter;
import uk.co.jakebreen.shushevents.presenter.loader.PresenterFactory;
import uk.co.jakebreen.shushevents.view.ImagePickerView;

public final class ImagePickerActivity extends BaseActivity<ImagePickerPresenter, ImagePickerView> implements ImagePickerView {
    @Inject
    PresenterFactory<ImagePickerPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    private ListView lvCoverImageList;
    private Button btnCoverImageCancel, btnCoverImageUpload;
    private Uri resultUri;
    private String selectedCoverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        lvCoverImageList = (ListView) findViewById(R.id.lv_coverImageList);
        btnCoverImageCancel = (Button) findViewById(R.id.btn_coverImageCancel);
        btnCoverImageUpload = (Button) findViewById(R.id.btn_coverImageUpload);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getCoverImages();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerImagePickerViewComponent.builder()
                .appComponent(parentComponent)
                .imagePickerViewModule(new ImagePickerViewModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<ImagePickerPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void cropImageTask() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,1)
                .setRequestedSize(660,165, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                //.setMinCropResultSize(330,60)
                //.setMaxCropResultSize(330,60)
                .start(this);
    }

    @Override
    public void displayCoverImageList(ArrayList<String> coverImageList) {
        // Construct the data source
        ArrayList<String> arrayListCoverImage = new ArrayList<String>(coverImageList);
        // Create the adapter to convert the array to views
        CoverImageAdapter adapter = new CoverImageAdapter(this, arrayListCoverImage);
        // Attach the adapter to a ListView
        lvCoverImageList.setAdapter(adapter);
    }

    public class CoverImageAdapter extends ArrayAdapter<String> {

        public CoverImageAdapter(Context context, ArrayList<String> arrayListCoverImage) {
            super(context, 0, arrayListCoverImage);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String coverImage = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coverimage_list, parent, false);
            }

            // Lookup view for data population
            ImageView ivCoverImage = (ImageView) convertView.findViewById(R.id.iv_coverImage);

            Picasso.get().load("http://jakebreen.co.uk/android/shushevents/classimages/" + coverImage).fit().into(ivCoverImage);

            lvCoverImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {
                    resultUri = null;
                    selectedCoverImage = (lvCoverImageList.getItemAtPosition(position).toString());

                    Intent intent = new Intent();
                    intent.putExtra("selectImage", selectedCoverImage);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedCoverImage = null;
                resultUri = result.getUri();

                Intent intent = new Intent();
                intent.putExtra("uriResult", resultUri.toString());
                setResult(RESULT_OK, intent);
                finish();
                //iv_createEventImageHolder.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @OnClick(R.id.btn_coverImageCancel)
    public void onClickCancel() {
        finish();
    }

    @OnClick(R.id.btn_coverImageUpload)
    public void onClickUpload() {
        cropImageTask();
    }
}
