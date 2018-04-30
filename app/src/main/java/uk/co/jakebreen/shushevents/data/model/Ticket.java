package uk.co.jakebreen.shushevents.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jake on 25/03/2018.
 */

public class Ticket implements Serializable {

    @SerializedName("idticket")
    @Expose
    private int idticket;
    @SerializedName("eventid")
    @Expose
    private int eventid;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("entryFee")
    @Expose
    private String entryFee;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("txdatetime")
    @Expose
    private String txdatetime;

    @SerializedName("txid")
    @Expose
    private String txid;
    @SerializedName("entrants")
    @Expose
    private int entrants;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("refunded")
    @Expose
    private int refunded;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("handle")
    @Expose
    private String handle;

    public int getIdticket() {
        return idticket;
    }

    public void setIdticket(int idticket) {
        this.idticket = idticket;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTxdatetime() {
        return txdatetime;
    }

    public void setTxdatetime(String txdatetime) {
        this.txdatetime = txdatetime;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getEntrants() {
        return entrants;
    }

    public void setEntrants(int entrants) {
        this.entrants = entrants;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getRefunded() {
        return refunded;
    }

    public void setRefunded(int refunded) {
        this.refunded = refunded;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "idticket=" + idticket +
                ", eventid=" + eventid +
                ", userid='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", entryFee='" + entryFee + '\'' +
                ", duration='" + duration + '\'' +
                ", txdatetime='" + txdatetime + '\'' +
                ", txid='" + txid + '\'' +
                ", entrants=" + entrants +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", refunded=" + refunded +
                ", coverImage='" + coverImage + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", handle='" + handle + '\'' +
                '}';
    }
}
