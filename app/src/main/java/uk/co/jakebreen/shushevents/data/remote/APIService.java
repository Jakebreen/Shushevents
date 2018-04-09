package uk.co.jakebreen.shushevents.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import uk.co.jakebreen.shushevents.data.model.Account;
import uk.co.jakebreen.shushevents.data.model.Event;
import uk.co.jakebreen.shushevents.data.model.Instructor;
import uk.co.jakebreen.shushevents.data.model.Role;
import uk.co.jakebreen.shushevents.data.model.Ticket;
import uk.co.jakebreen.shushevents.data.model.Venue;

/**
 * Created by Jake on 02/03/2018.
 */

public interface APIService {

    @POST("android/shushevents/insert/insertAccount.php")
    @FormUrlEncoded
    Call<Account> saveAccount(@Field("userid") String userid,
                              @Field("firstname") String firstname,
                              @Field("surname") String surname,
                              @Field("email") String email,
                              @Field("roleid") int roleid);

    @POST("android/shushevents/insert/insertEvent.php")
    @FormUrlEncoded
    Call<Event> saveEvent(@Field("userid") String userid,
                          @Field("title") String title,
                          @Field("description") String description,
                          @Field("instructorId") String instructorId,
                          @Field("date") String date,
                          @Field("time") String time,
                          @Field("entryFee") String entryFee,
                          @Field("duration") String duration,
                          @Field("maxTickets") int maxTickets,
                          @Field("venueId") int venueId,
                          @Field("repeatWeeks") String repeatWeeks);

    @POST("android/shushevents/select/selectAccountByEmail.php")
    @FormUrlEncoded
    Call<List<Account>> getAccountByEmail(@Field("email") String email);

    @GET("android/shushevents/select/selectRole.php")
    Call<List<Role>> getRole();

    @POST("android/shushevents/select/selectAccountByUserID.php")
    @FormUrlEncoded
    Call<List<Account>> getAccountByUserID(@Field("userid") String userid);

    @POST("android/shushevents/update/updateRole.php")
    @FormUrlEncoded
    Call<Account> updateAccountRole(@Field("userid") String userid,
                           @Field("roleid") int roleid);

    @POST("android/shushevents/insert/insertVenue.php")
    @FormUrlEncoded
    Call<Venue> saveVenue(@Field("handle") String handle,
                          @Field("address") String address,
                          @Field("town") String town,
                          @Field("postcode") String postcode,
                          @Field("lat") Double lat,
                          @Field("lng") Double lng);

    @GET("android/shushevents/select/selectVenue.php")
    Call<List<Venue>> getVenue();

    @POST("android/shushevents/select/selectVenueById.php")
    @FormUrlEncoded
    Call<List<Venue>> getVenueById(@Field("venueid") int venueid);

    @GET("android/shushevents/select/selectInstructor.php")
    Call<List<Instructor>> getInstructors();

    @POST("android/shushevents/select/selectEventByLatLng.php")
    @FormUrlEncoded
    Call<List<Event>> getEventByLatLng(@Field("lat") Double lat,
                                       @Field("lng") Double lng);

    @POST("android/shushevents/insert/insertTicket.php")
    @FormUrlEncoded
    Call<Ticket> saveTicket(@Field("userid") String userid,
                            @Field("idevent") int idevent,
                            @Field("txid") String txid,
                            @Field("txDateTime") String txDateTime,
                            @Field("entrants") int entrants);

    @POST("android/shushevents/select/selectTicketsAvailable.php")
    @FormUrlEncoded
    Call<String> getRemainingTickets(@Field("idevent") int idevent);

    @POST("android/shushevents/select/selectPaidEventsByUserID.php")
    @FormUrlEncoded
    Call<List<Ticket>> getPaidEventByUserID(@Field("userid") String userid);

    @POST("android/shushevents/select/selectEventByDate.php")
    @FormUrlEncoded
    Call<List<Event>> getEventByDate(@Field("date") String date);

    @POST("android/shushevents/braintreepayments/ticketRefund.php")
    @FormUrlEncoded
    Call<String> cancelEvent(@Field("eventId") int eventid);

}
