package uk.co.jakebreen.shushevents.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jake on 02/03/2018.
 */

public class Account implements Parcelable, Serializable {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("roleid")
    @Expose
    private int roleid;
    @SerializedName("firebaseToken")
    @Expose
    private String firebaseToken;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userid='" + userid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", roleid=" + roleid +
                ", firebaseToken='" + firebaseToken + '\'' +
                '}';
    }

    protected Account(Parcel in) {
        userid = in.readString();
        firstname = in.readString();
        surname = in.readString();
        email = in.readString();
        roleid = in.readInt();
        firebaseToken = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeString(firstname);
        parcel.writeString(surname);
        parcel.writeString(email);
        parcel.writeInt(roleid);
        parcel.writeString(firebaseToken);
    }
}
