package com.uk.iot.model;

/**
 * Created by Senthil on 04/06/2020.
 */
public class LoadDataRequest {
    private String filePath;

    public LoadDataRequest() {}
    public LoadDataRequest(String filePath) {
        super();
        this.filePath = filePath;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
