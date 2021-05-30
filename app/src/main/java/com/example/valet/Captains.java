package com.example.valet;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Captains {

    public String captainName;
    public String captain_rate;
    public String captainNumber;
    public String captainPic;

    public String clientName;
    public String clientPhone;

    public String serial;

    public Captains(String captainName, String captain_rate, String captainNumber, String captainPic) {
        this.captainName = captainName;
        this.captain_rate = captain_rate;
        this.captainNumber = captainNumber;
        this.captainPic = captainPic;
    }

    public Captains() {

    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getCaptain_rate() {
        return captain_rate;
    }

    public void setCaptain_rate(String captain_rate) {
        this.captain_rate = captain_rate;
    }

    public String getCaptainNumber() {
        return captainNumber;
    }

    public void setCaptainNumber(String captainNumber) {
        this.captainNumber = captainNumber;
    }

    public String getCaptainPic() {
        return captainPic;
    }

    public void setCaptainPic(String captainPic) {
        this.captainPic = captainPic;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("CAPTAIN_NAME", "'" + captainName + "'");
            obj.put("CAPTAIN_NO", "'" +captainNumber+ "'");
            obj.put("CLIENT_NAME", "'" +clientName+ "'");
            obj.put("CLIENT_NO", "'" +clientPhone+ "'");

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
