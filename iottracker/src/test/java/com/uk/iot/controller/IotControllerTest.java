package com.uk.iot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uk.iot.domain.DataLoader;
import com.uk.iot.model.Body;
import com.uk.iot.model.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Senthil on 03/06/2020.
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({IotController.class, DataLoader.class})
public class IotControllerTest {
    @Mock
    DataLoader dataLoader;

    IotController iotController;

    @Before
    public void setUp() throws Exception{
        iotController = new IotController();
        PowerMockito.whenNew(DataLoader.class).withAnyArguments().thenReturn(dataLoader);
    }

    @Test
    public void testLoadData_Success() throws Exception {
        String filePath = "/Users/Dummy/Downloads/data.csv";
        when(dataLoader.loadData(filePath)).thenReturn(getLoadDataResult("200 OK", "data refreshed"));
        String response = iotController.loadData(filePath);
        assertNotNull(response);
        assertEquals(" Invalid status code ", getExpectedResult("200 OK", "data refreshed"),response);
    }

    @Test
    public void testLoadData_IncorrectPath() throws Exception {
        String filePath = "/Users/Dummy/Downloads/data.c";
        when(dataLoader.loadData(filePath)).thenReturn(getLoadDataResult("404 Not Found", "ERROR: no data file found"));
        String response = iotController.loadData(filePath);
        assertNotNull(response);
        assertEquals(" Invalid Exception ", getExpectedResult("404 Not Found", "ERROR: no data file found"),response);
    }

    @Test
    public void testLoadData_Miscellaneous_Exception() throws Exception {
        String filePath = "/Users/Dummy/Downloads/data.csv";
        when(dataLoader.loadData(filePath)).thenReturn(getLoadDataResult("500", "ERROR: A technical exception occurred"));
        String response = iotController.loadData(filePath);
        assertNotNull(response);
        assertEquals(" Invalid Exception ", getExpectedResult("500", "ERROR: A technical exception occurred"),response);
    }

    private Result getLoadDataResult(String httpStatus, String description) {
        Body body = new Body();
        body.setDescription(description);
        return new Result(httpStatus, body);
    }

    private String getExpectedResult(String status, String description) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(getLoadDataResult(status, description));
    }
}
