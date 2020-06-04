package com.uk.iot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Senthil on 03/06/2020.
 */
//@Data
//@AllArgsConstructor
public class Result {
    private String httpStatus;
    private Body body;

    public Result(String httpStatus, Body body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }
}
