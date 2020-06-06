package com.uk.iot.model;

/**
 * Created by Senthil on 05/06/2020.
 */
public class DeviceLocationResult {
    private String httpStatus;
    private Body deviceLocationDetails;

    public DeviceLocationResult(String httpStatus, Body deviceLocationDetails) {
        this.httpStatus = httpStatus;
        this.deviceLocationDetails = deviceLocationDetails;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public Body getDeviceLocationDetails() {
        return deviceLocationDetails;
    }
}
