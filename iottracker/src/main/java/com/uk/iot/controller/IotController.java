package com.uk.iot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uk.iot.domain.DataLoader;
import com.uk.iot.domain.ReportDevice;
import com.uk.iot.model.DeviceLocationResult;
import com.uk.iot.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Senthil on 03/06/2020.
 */
@RestController
@Component
public class IotController {
    static final Logger logger = LogManager.getLogger(IotController.class);

    /**
     * **** Part 1 MVP: IoT batch data loading service *****
     * Service to load the IOT device location details to a backend platform.
     * The data is stored in a in-memory dataset.
     */
    @PostMapping("/iot/event/v1")
    @ApiOperation(value = "Load IOT devices",
    notes = "Load all the IOT devices into an in-memory dataset ",
    response = String.class)
    public String loadData(@ApiParam(value = "PLease provide the csv filePath to retrieve and load the IOT device details. " +
            "Sample filePath would be like this - /Users/Dummy/data.csv")
                               @RequestBody String filePath) {
        logger.info("Load Data service is invoked ");
        DataLoader dataLoader = new DataLoader();
        Result result = dataLoader.loadData(filePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }

    /**
     * **** Part 2 MVP+1: Report device details and location *****
     * Service to locate the IOT device for the given productId. Optionally, by passing specific timestamp we will
     * get the exact location of the device.
     *
     * **** Part 3 MVP+2: Dynamic activity-tracking *****
     * CyclePlusTracker user will now able to easily find whether they are actively cycling or resting.
     * The device status will be reflected accordingly.
     */
    @RequestMapping(value ="/iot/event/v1", params = {"productId"}, method = RequestMethod.GET)
    @ApiOperation(value = "Report device location",
            notes = "Locate the IOT device for the matching productId and an optional timestamp parameter ",
            response = String.class)
    public String getLocationDetails(@ApiParam(value = "Please provide the productId to retrieve the latest location " +
            "details of the IOT device. Additionally, you can pass the exact timeStamp to get the specific location details " +
            "of the device.")
            @RequestParam("productId") String productId, @RequestParam(required = false) String tstmp) {
        logger.info("Report IOT device location service is invoked ");
        ReportDevice reportDevice = new ReportDevice();
        DeviceLocationResult deviceLocationResult = reportDevice.getDeviceLocation(productId, tstmp);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(deviceLocationResult);
    }
}
