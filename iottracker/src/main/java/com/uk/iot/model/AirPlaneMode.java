package com.uk.iot.model;

/**
 * Created by Senthil on 05/06/2020.
 */
public enum AirPlaneMode {
    ON("SUCCESS: Location not available: Please turn off airplane mode"),
    OFF("SUCCESS: Location identified.");

    private String description;
    AirPlaneMode(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
