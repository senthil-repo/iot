package com.uk.iot.model;

import lombok.Data;

import java.sql.Timestamp;


/**
 * Created by Senthil on 03/06/2020.
 */
//@Data
public class IOTDevice {
    private Timestamp dateTime;
    private int eventID;
    private String productID;
    private String latitude;
    private String longitude;
    private double battery;
    private String light;
    private String airPlaneMode;

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getAirPlaneMode() {
        return airPlaneMode;
    }

    public void setAirPlaneMode(String airPlaneMode) {
        this.airPlaneMode = airPlaneMode;
    }
}
