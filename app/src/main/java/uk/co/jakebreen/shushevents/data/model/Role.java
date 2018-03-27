package uk.co.jakebreen.shushevents.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jake on 10/03/2018.
 */

public class Role {

    @SerializedName("idrole")
    @Expose
    private int idrole;
    @SerializedName("title")
    @Expose
    private String title;

    public int getIdRole() {
        return idrole;
    }

    public void setIdRole(int idRole) {
        this.idrole = idRole;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Role{" +
                "idRole=" + idrole +
                ", title='" + title + '\'' +
                '}';
    }
}
