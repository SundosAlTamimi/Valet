package com.example.valet;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Clients {

    public String userName;
    public String password;
    public String e_mail;
    public String phoneNumber;
    public String carType;
    public String carModel;
    public String carColor;
    public String carLot;
    public Bitmap userPic;
    public Bitmap carPic;

    public String date;
    public String time;
    public String latitude;
    public String longitude;

    public String captainName;
    public String captainNumber;

    public Clients(String userName, String password, String e_mail, String phoneNumber, String carType, String carModel, String carColor, String carLot, Bitmap userPic, Bitmap carPic) {
        this.userName = userName;
        this.password = password;
        this.e_mail = e_mail;
        this.phoneNumber = phoneNumber;
        this.carType = carType;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carLot = carLot;
        this.userPic = userPic;
        this.carPic = carPic;
    }

    public Clients() {

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarLot() {
        return carLot;
    }

    public void setCarLot(String carLot) {
        this.carLot = carLot;
    }

    public Bitmap getUserPic() {
        return userPic;
    }

    public void setUserPic(Bitmap userPic) {
        this.userPic = userPic;
    }

    public Bitmap getCarPic() {
        return carPic;
    }

    public void setCarPic(Bitmap carPic) {
        this.carPic = carPic;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getCaptainNumber() {
        return captainNumber;
    }

    public void setCaptainNumber(String captainNumber) {
        this.captainNumber = captainNumber;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("USERNAME", "'" + userName + "'");
            obj.put("PASSWORD", "'" +password+ "'");
            obj.put("E_MAIL", "'" +e_mail+ "'");
            obj.put("PHONE_NO", "'" +phoneNumber+ "'");
            obj.put("CAR_TYPE", "'" +carType+ "'");
            obj.put("CAR_MODEL", "'" +carModel+ "'");
            obj.put("CAR_COLOR", "'" +carColor+ "'");
            obj.put("CAR_LOT", "'" +carLot+ "'");


        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject2() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("USERNAME", "'" + userName + "'");
            obj.put("PASSWORD", "'" +password+ "'");
            obj.put("E_MAIL", "'" +e_mail+ "'");
            obj.put("PHONE_NO", "'" +phoneNumber+ "'");
            obj.put("CAR_TYPE", "'" +carType+ "'");
            obj.put("CAR_MODEL", "'" +carModel+ "'");
            obj.put("CAR_COLOR", "'" +carColor+ "'");
            obj.put("CAR_LOT", "'" +carLot+ "'");
            obj.put("TIME", "'" +time+ "'");
            obj.put("_DATE", "'" +date+ "'");
            obj.put("LAT", "'" +latitude+ "'");
            obj.put("LONG", "'" +longitude+ "'");


        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject3() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("USERNAME", "'" + userName + "'");
            obj.put("PASSWORD", "'" +password+ "'");
            obj.put("E_MAIL", "'" +e_mail+ "'");
            obj.put("PHONE_NO", "'" +phoneNumber+ "'");
            obj.put("CAR_TYPE", "'" +carType+ "'");
            obj.put("CAR_MODEL", "'" +carModel+ "'");
            obj.put("CAR_COLOR", "'" +carColor+ "'");
            obj.put("CAR_LOT", "'" +carLot+ "'");
            obj.put("TIME", "'" +time+ "'");
            obj.put("_DATE", "'" +date+ "'");
            obj.put("CAPTAIN_NAME", "'" +captainName+ "'");
            obj.put("CAPTAIN_NO", "'" +captainNumber+ "'");


        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }


}
