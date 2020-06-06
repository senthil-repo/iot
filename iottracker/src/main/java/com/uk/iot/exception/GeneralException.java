package com.uk.iot.exception;

/**
 * Created by Appa on 03/06/2020.
 */
public class GeneralException extends RuntimeException{
    private int errorCode;
    private String errorDescription;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

   public GeneralException(int errorCode, String description) {
        this.errorCode = errorCode;
        this.errorDescription = description;
    }
}
