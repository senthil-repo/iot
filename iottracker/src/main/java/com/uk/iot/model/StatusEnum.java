package com.uk.iot.model;

/**
 * Created by Senthil on 05/06/2020.
 */
public enum StatusEnum {
    ACTIVE("Active"), INACTIVE("Inactive"), NOT_APPLICABLE("N/A");

    private String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
