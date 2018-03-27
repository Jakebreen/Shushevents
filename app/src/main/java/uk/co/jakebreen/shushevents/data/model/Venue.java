package uk.co.jakebreen.shushevents.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jake on 05/03/2018.
 */

public class Venue implements Serializable {

    @SerializedName("idvenue")
    @Expose
    private int idvenue;
    @SerializedName("handle")
    @Expose
    private String handle;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public int getVenueId() {
        return idvenue;
    }

    public void setVenueId(int idvenue) {
        this.idvenue = idvenue;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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

    //@Override
    //public String toString() {
    //    return "Venue{" +
    //            "idvenue=" + idvenue +
    //            ", handle='" + handle + '\'' +
    //            ", address='" + address + '\'' +
    //            ", town='" + town + '\'' +
    //            ", postcode='" + postcode + '\'' +
    //            ", lat=" + lat +
    //            ", lng=" + lng +
    //            '}';
    //}


    @Override
    public String toString() {
        return handle + ", " + address + " " + postcode;
    }
}
