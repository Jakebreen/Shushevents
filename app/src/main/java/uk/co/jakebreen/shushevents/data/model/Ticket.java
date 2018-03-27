package uk.co.jakebreen.shushevents.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jake on 25/03/2018.
 */

public class Ticket {

    @SerializedName("idticket")
    @Expose
    private int idticket;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("idevent")
    @Expose
    private int idevent;
    @SerializedName("txid")
    @Expose
    private String txid;
    @SerializedName("txDateTime")
    @Expose
    private String txDateTime;
    @SerializedName("entrants")
    @Expose
    private int entrants;

    public int getIdticket() {
        return idticket;
    }

    public void setIdticket(int idticket) {
        this.idticket = idticket;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getTxDateTime() {
        return txDateTime;
    }

    public void setTxDateTime(String txDateTime) {
        this.txDateTime = txDateTime;
    }

    public int getEntrants() {
        return entrants;
    }

    public void setEntrants(int entrants) {
        this.entrants = entrants;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "idticket=" + idticket +
                ", userid='" + userid + '\'' +
                ", idevent=" + idevent +
                ", txid='" + txid + '\'' +
                ", txDateTime='" + txDateTime + '\'' +
                ", entrants=" + entrants +
                '}';
    }
}
