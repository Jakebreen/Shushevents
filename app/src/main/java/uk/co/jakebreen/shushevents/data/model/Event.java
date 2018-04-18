package uk.co.jakebreen.shushevents.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jake on 02/03/2018.
 */

public class Event implements Serializable {

    @SerializedName("idevent")
    @Expose
    private int idevent;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("instructorId")
    @Expose
    private String instructorId;
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
    @SerializedName("maxTickets")
    @Expose
    private int maxTickets;
    @SerializedName("venueid")
    @Expose
    private int venueid;
    @SerializedName("distance")
    @Expose
    private Float distance;
    @SerializedName("entrants")
    @Expose
    private int entrants;
    @SerializedName("instructorFirstname")
    @Expose
    private String instructorFirstname;
    @SerializedName("instructorSurname")
    @Expose
    private String instructorSurname;
    @SerializedName("venueHandle")
    @Expose
    private String venueHandle;
    @SerializedName("visible")
    @Expose
    private int visible;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;

    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
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

    public int getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }

    public int getVenueid() {
        return venueid;
    }

    public void setVenueid(int venueid) {
        this.venueid = venueid;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public int getEntrants() {
        return entrants;
    }

    public void setEntrants(int entrants) {
        this.entrants = entrants;
    }

    public String getInstructorFirstname() {
        return instructorFirstname;
    }

    public void setInstructorFirstname(String instructorFirstname) {
        this.instructorFirstname = instructorFirstname;
    }

    public String getInstructorSurname() {
        return instructorSurname;
    }

    public void setInstructorSurname(String instructorSurname) {
        this.instructorSurname = instructorSurname;
    }

    public String getVenueHandle() {
        return venueHandle;
    }

    public void setVenueHandle(String venueHandle) {
        this.venueHandle = venueHandle;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "Event{" +
                "idevent=" + idevent +
                ", userid='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", instructorId='" + instructorId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", entryFee='" + entryFee + '\'' +
                ", duration='" + duration + '\'' +
                ", maxTickets=" + maxTickets +
                ", venueid=" + venueid +
                ", distance=" + distance +
                ", entrants=" + entrants +
                ", instructorFirstname='" + instructorFirstname + '\'' +
                ", instructorSurname='" + instructorSurname + '\'' +
                ", venueHandle='" + venueHandle + '\'' +
                ", visible=" + visible +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }
}
