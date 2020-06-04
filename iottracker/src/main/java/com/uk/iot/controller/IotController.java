package com.uk.iot.controller;

import com.uk.iot.domain.DataLoader;
import com.uk.iot.model.Body;
import com.uk.iot.model.LoadDataRequest;
import com.uk.iot.model.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Senthil on 03/06/2020.
 */
@RestController
@Component
//@RequestMapping(path = "/iot/event/v1")
public class IotController {

    //@PostMapping(path = "/iot/event/v1", consumes = "application/json", produces = "application/json")
    @PostMapping("/iot/event/v1")
    //public Result loadData(@RequestBody LoadDataRequest loadDataRequest) {
    public Result loadData() {
        System.out.println(" Inside &&&&&&&&&&&&& ");
        DataLoader dataLoader = new DataLoader();
        return dataLoader.loadData("");
    }

    @GetMapping("/products")
    public Result getLocationDetails() {
        return new Result("Sucess", new Body());
    }
}
