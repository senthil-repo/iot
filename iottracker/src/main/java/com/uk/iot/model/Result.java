package com.uk.iot.model;

/**
 * Created by Senthil on 03/06/2020.
 */

public class Result {
    private String httpStatus;
    private Body body;

    public Result(String httpStatus, Body body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public String getHttpStatus() {
        return httpStatus;
    }
}
