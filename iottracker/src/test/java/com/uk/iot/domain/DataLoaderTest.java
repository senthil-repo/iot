package com.uk.iot.domain;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import org.mockito.AdditionalAnswers;

/**
 * Created by Senthil on 03/06/2020.
 */
@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({DataLoader.class, BufferedReader.class})
public class DataLoaderTest {

    DataLoader dataLoader;

    @Mock
    FileReader fileReader;

    @Mock
    private BufferedReader bufferedReader;

    @Before
    public void setUp() throws Exception{
        dataLoader = new DataLoader();
    }

    @Test
    public void testLoadDtestLoadData_Successata_Success() throws Exception{
        String filePath = "/Users/Dummy/Downloads/data.csv";
        PowerMockito.whenNew(FileReader.class).withAnyArguments().thenReturn(fileReader);
        PowerMockito.whenNew(BufferedReader.class).withAnyArguments().thenReturn(bufferedReader);
        when(bufferedReader.readLine()).thenAnswer(AdditionalAnswers.returnsElementsOf(getRecords()));
        Result result = dataLoader.loadData(filePath);
        assertNotNull(result);
        assertEquals(" Unexpected result ", getExpected_SuccessResult().getHttpStatus(), result.getHttpStatus());
    }

    @Test
    public void testLoadData_NoDataFileFound() {
        String filePath = "/Users/Dummy/Downloads/data.c";
        Result result = dataLoader.loadData(filePath);
        assertNotNull(result);
        assertEquals(" Invalid result ", getExpected_NoDataFound_Result().getHttpStatus(), result.getHttpStatus());
    }

    @Test
    public void testLoadData_GeneralException() throws Exception{
        String filePath = "/Users/Dummy/Downloads/data.csv";
        PowerMockito.whenNew(FileReader.class).withAnyArguments().thenReturn(null);
        Result result = dataLoader.loadData(filePath);
        assertNotNull(result);
        assertEquals(" Wrong, Excepted a general exception ", getExpected_GeneralException_Result().getHttpStatus(), result.getHttpStatus());
    }

    private List<String> getRecords() {
        List<String> records = new ArrayList<>();
        records.add("1582605077000,10001,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
        records.add("1582605137000,10002,WG11155638,51.5185,-0.1736,0.99,OFF,OFF");
        records.add("1582605197000,10003,WG11155638,51.5185,-0.1736,0.98,OFF,OFF");
        records.add("1582605257000,10004,WG11155638,51.5185,-0.1736,0.98,OFF,OFF");
        records.add("1582605257000,10005,6900001001,40.73061,-73.935242,0.11,N/A,OFF");
        records.add("1582605258000,10006,6900001001,40.73071,-73.935242,0.1,N/A,OFF");
        records.add("1582605259000,10007,6900001001,40.73081,-73.935242,0.1,N/A,OFF");
        records.add("1582605317000,10008,WG11155800,45.5185,-12.52029,0.11,ON,OFF");
        records.add("1582605377000,10009,WG11155800,45.5186,-12.52027,0.1,ON,OFF");
        records.add("1582605437000,10010,WG11155800,45.5187,-12.52025,0.09,ON,OFF");
        records.add("1582605497000,10011,WG11155638,51.5185,-0.17538,0.95,OFF,OFF");
        records.add("1582605557000,10012,6900001001,40.73081,-73.935242,0.1,N/A,OFF");
        records.add("1582605615000,10013,6900233111,,,0.1,N/A,ON");
        records.add("1582612875000,10014,6900233111,,,0.1,N/A,OFF");
        records.add(null);

        return records;
    }

    private Result getExpected_SuccessResult(){
        Body body = new Body();
        body.setDescription("data refreshed");
        return new Result("200 OK", body);
    }

    private Result getExpected_NoDataFound_Result(){
        Body body = new Body();
        body.setDescription("ERROR: no data file found");
        return new Result("404 Not Found", body);
    }

    private Result getExpected_GeneralException_Result(){
        Body body = new Body();
        body.setDescription("ERROR: A technical exception occurred. Once the issue is resolved, please reload the data.");
        return new Result("500 Internal Server Error", body);
    }
}
