package uk.co.jakebreen.shushevents.presenter.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.data.model.Instructor;
import uk.co.jakebreen.shushevents.data.model.Venue;
import uk.co.jakebreen.shushevents.data.remote.APIService;
import uk.co.jakebreen.shushevents.data.remote.ApiUtils;
import uk.co.jakebreen.shushevents.interactor.CreateEventInteractor;
import uk.co.jakebreen.shushevents.presenter.CreateEventPresenter;
import uk.co.jakebreen.shushevents.view.CreateEventView;

public final class CreateEventPresenterImpl extends BasePresenterImpl<CreateEventView> implements CreateEventPresenter {

    private String TAG = CreateEventPresenterImpl.class.getSimpleName();

    /**
     * The interactor
     */
    @NonNull
    private final CreateEventInteractor mInteractor;

    // The view is available using the mView variable

    private APIService mAPIService;

    @Inject
    public CreateEventPresenterImpl(@NonNull CreateEventInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
        mAPIService = ApiUtils.getAPIService();
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
    public boolean validateForm(String title, String description, String instructor, String date,
                                String time, String ticketPrice, String ticketMax, Venue venue,
                                String duration, Uri uri, String selectedCoverImage) {

        if (title.equals(null) || title.equals("")) {
            mView.showToast("Title field empty");
            return false;
        } else if (description.equals(null) || description.equals("")) {
            mView.showToast("Description field empty");
            return false;
        } else if (instructor.equals(null) || instructor.equals("")) {
            mView.showToast("Instructor field empty");
            return false;
        } else if (date.equals(null) || date.equals("")) {
            mView.showToast("Date field empty");
            return false;
        } else if (time.equals(null) || time.equals("")) {
            mView.showToast("Time field empty");
            return false;
        } else if (ticketPrice.equals(null) || ticketPrice.equals("")) {
            mView.showToast("Ticket fee field empty");
            return false;
        } else if (ticketMax.equals(null) || ticketMax.equals("")) {
            mView.showToast("Max tickets field empty");
            return false;
        } else if (venue == null) {
            mView.showToast("Venue not select");
            return false;
        } else if (duration.equals(null) || duration.equals("")) {
            mView.showToast("Duration field empty");
            return false;
        } else if ((uri == null || uri.equals("")) && (selectedCoverImage.equals(null) || selectedCoverImage.equals(""))) {
            mView.showToast("No cover image selected");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void sendEvent(String userid, String title, String description, String instructor,
                          String date, String time, String ticketPrice, int ticketMax, int venueId,
                          String duration, String repeatWeeks, Uri uri, String selectedCoverImage) {

        String fileName;

        if (uri == null || uri.equals("")) {
            fileName = selectedCoverImage;
        } else {
            File file = new File(uri.getPath());
            file.getAbsolutePath();
            fileName = file.getName();
            sendCoverImage(uri);
        }

        mAPIService.saveEvent(userid, title, description, instructor, date, time, ticketPrice,
                duration, ticketMax, venueId, repeatWeeks, fileName).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "event submitted to API." + response.body().toString());
                    mView.showToast("New event created");
                    mView.clearForm();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit event to API.");
                mView.showToast("Error creating event, " + t);
            }
        });
    }

    public void showResponse(String response) {
        Log.e(TAG, "Response: " + response);
    }

    @Override
    public void getInstructors() {
        mAPIService.getInstructors().enqueue(new Callback<List<Instructor>>() {
            @Override
            public void onResponse(Call<List<Instructor>> call, Response<List<Instructor>> response) {
                if(response.isSuccessful()) {
                    showResponse(response.body().toString());

                    List<Instructor> instructorList = response.body();
                    if (mView == null) {
                        Log.i(TAG, "mView is null." + response.body().toString());
                    } else {
                        mView.displayInstructorSpinner(instructorList);
                    }



                    Log.i(TAG, "Instructor request submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Instructor>> call, Throwable t) {
                mView.showToast("Unable to find instructors");
                showResponse(t.toString());
                Log.e(TAG, "Unable to submit instructor request to API.");
            }
        });
    }

    @Override
    public String onDateSetFormatDate(int startYear, int startMonth, int startDay) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.e(TAG, String.valueOf(currentTime));

        Calendar calander = Calendar.getInstance();
        calander.setTimeInMillis(0);
        calander.set(startYear, startMonth, startDay, 0, 0, 0);
        Date SelectedDate = calander.getTime();

        if (SelectedDate.before(currentTime)) {
            mView.showToast("Select a future date");
            return null;
        } else {
            DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String StringDateformat_UK = dateformat_UK.format(SelectedDate);
            return StringDateformat_UK;
        }
    }

    //Upload cover image to server
    @Override
    public void sendCoverImage(Uri uri) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        APIService apiService = new Retrofit.Builder().baseUrl("http://jakebreen.co.uk/").client(client).build().create(APIService.class);

        File file = new File(uri.getPath());
        file.getAbsolutePath();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        retrofit2.Call<okhttp3.ResponseBody> req = apiService.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Do Something
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}