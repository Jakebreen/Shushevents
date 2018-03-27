package uk.co.jakebreen.shushevents.data.remote;

/**
 * Created by Jake on 02/03/2018.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://jakebreen.co.uk/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
