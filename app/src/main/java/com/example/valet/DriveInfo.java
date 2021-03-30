package com.example.valet;

public class DriveInfo {

    private String driverName;
    private String driverPlace;

    public DriveInfo() {

    }

    public DriveInfo(String driverName, String driverPlace) {
        this.driverName = driverName;
        this.driverPlace = driverPlace;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPlace() {
        return driverPlace;
    }

    public void setDriverPlace(String driverPlace) {
        this.driverPlace = driverPlace;
    }
}
