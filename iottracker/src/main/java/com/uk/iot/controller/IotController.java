package com.uk.iot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uk.iot.cache.IOTDeviceCache;
import com.uk.iot.domain.DataLoader;
import com.uk.iot.model.Body;
import com.uk.iot.model.LoadDataRequest;
import com.uk.iot.model.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * Created by Senthil on 03/06/2020.
 */
@RestController
//@RequestMapping(path = "/iot/event/v1")
public class IotController {

    //@PostMapping(path = "/iot/event/v1", consumes = "application/json", produces = "application/json")
    @PostMapping("/iot/event/v1")
    //public Result loadData(@RequestBody LoadDataRequest loadDataRequest) { //TODO - use JSON request
    public String loadData(@RequestBody String filePath) {
        System.out.println(" Inside loadData ");
        DataLoader dataLoader = new DataLoader();
        //return dataLoader.loadData(loadDataRequest.getFilePath());
        Result result = dataLoader.loadData(filePath);
        //getAll();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }

    @RequestMapping(value ="/iot/event/v1", params = {"productId"}, method = RequestMethod.GET)
    public String getLocationDetails(@RequestParam("productId") String productId, @RequestParam(required = false) String tstmp) {
        System.out.println(" productId "+productId + " ---- "+tstmp);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Result result = new Result("Success", new Body());
        return gson.toJson(result);
    }

/*    private void getAll() {
        IOTDeviceCache.getIOTDevices();
    }*/

}
