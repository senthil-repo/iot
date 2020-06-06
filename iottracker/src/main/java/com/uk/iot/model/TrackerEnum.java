package com.uk.iot.model;

/**
 * Created by Senthil on 04/06/2020.
 */
public enum TrackerEnum {
    TR_WG("CyclePlusTracker"), TR_69("GeneralTracker");
    private String trackerName;

    TrackerEnum(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getTrackerName() {
        return trackerName;
    }
}
