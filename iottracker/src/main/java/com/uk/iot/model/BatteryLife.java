package com.uk.iot.model;

/**
 * Created by Senthil on 05/06/2020.
 */
public enum BatteryLife {
    FULL("Full"), HIGH("High"), MEDIUM("Medium"), LOW("Low"), CRITICAL("Critical");

    private String batteryLife;

    BatteryLife(String batteryLife) {
        this.batteryLife = batteryLife;
    }

    public String getBatteryLife() {
        return batteryLife;
    }
}
